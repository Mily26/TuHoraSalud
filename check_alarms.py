#!/usr/bin/env python3
"""
Inspect AlarmManager state for the TuHoraSalud Android app.

Runs `adb shell dumpsys alarm`, parses the output, and prints every alarm
the system currently has registered for com.example.apptuhorasalud, plus a
summary of recent fire history.

Usage:
    python check_alarms.py            # default package (TuHoraSalud)
    python check_alarms.py <package>  # any other package id

Standard library only.
"""

from __future__ import annotations

import re
import subprocess
import sys

DEFAULT_PACKAGE = "com.example.apptuhorasalud"

# Header line for a scheduled alarm block. Examples:
#     RTC_WAKEUP #15: Alarm{dfc4473 type 0 origWhen 1777766400000 whenElapsed 2997999 com.google.android.gms}
#     ELAPSED #2: Alarm{fabc79f type 3 origWhen 622847 whenElapsed 622847 com.google.android.gms}
HEADER_RE = re.compile(
    r"^(?P<indent> +)"
    r"(?P<kind>RTC|RTC_WAKEUP|ELAPSED|ELAPSED_WAKEUP)\s+"
    r"#(?P<index>\d+):\s+Alarm\{(?P<aid>\S+)\s+type\s+\d+\s+"
    r"origWhen\s+\S+(?:\s\S+)?\s+"
    r"whenElapsed\s+\S+\s+"
    r"(?P<pkg>[\w.]+)\}\s*$"
)

# Section heading like "  47 pending alarms:" or "  App Alarm history:".
SECTION_RE = re.compile(r"^ {0,4}(?P<name>\S[^:]*?):\s*(\(none\))?\s*$")

TAG_RE = re.compile(r"^\s*tag=(?P<tag>.+?)\s*$")
TYPE_LINE_RE = re.compile(r"^\s*type=\S+\s+origWhen=(?P<origWhen>.+?)\s+window=")
OPERATION_RE = re.compile(
    r"PendingIntentRecord\{\S+\s+(?P<target>\S+)\s+(?P<dispatch>\w+)\}"
)


def run_dumpsys() -> str:
    try:
        result = subprocess.run(
            ["adb", "shell", "dumpsys", "alarm"],
            capture_output=True,
            text=True,
            check=False,
        )
    except FileNotFoundError:
        sys.exit("error: 'adb' was not found on PATH. Install Android platform-tools.")

    if result.returncode != 0:
        msg = result.stderr.strip() or result.stdout.strip() or "unknown error"
        sys.exit(f"adb exited {result.returncode}: {msg}")

    return result.stdout


def parse_alarms(output: str, package: str):
    lines = output.splitlines()
    alarms = []
    current_section = "?"
    i = 0
    while i < len(lines):
        line = lines[i]

        section_match = SECTION_RE.match(line)
        if section_match and not HEADER_RE.match(line):
            current_section = section_match.group("name").strip()

        header_match = HEADER_RE.match(line)
        if header_match and header_match.group("pkg") == package:
            alarm = {
                "section": current_section,
                "kind": header_match.group("kind"),
                "id": header_match.group("aid"),
                "tag": None,
                "trigger": None,
                "operation": None,
            }

            header_indent = len(header_match.group("indent"))
            j = i + 1
            while j < len(lines):
                nxt = lines[j]
                if not nxt.strip():
                    break
                leading = len(nxt) - len(nxt.lstrip(" "))
                if leading <= header_indent:
                    break

                tag_match = TAG_RE.match(nxt)
                if tag_match:
                    alarm["tag"] = tag_match.group("tag")
                type_match = TYPE_LINE_RE.match(nxt)
                if type_match:
                    alarm["trigger"] = type_match.group("origWhen").strip()
                op_match = OPERATION_RE.search(nxt)
                if op_match:
                    alarm["operation"] = (
                        f"{op_match.group('target')} ({op_match.group('dispatch')})"
                    )
                j += 1

            alarms.append(alarm)
            i = j
            continue

        i += 1

    return alarms


def parse_fire_history(output: str, package: str):
    """Return the recent-fire-time list from 'App Alarm history' for this package."""
    in_history = False
    for line in output.splitlines():
        section_match = SECTION_RE.match(line)
        if section_match and section_match.group("name").strip() == "App Alarm history":
            in_history = True
            continue
        if not in_history:
            continue
        # History entries are indented further than the section header (2 spaces).
        if line and not line.startswith("    "):
            break
        stripped = line.strip()
        if stripped.startswith(package + ","):
            return [t.strip() for t in stripped.split(",")[1:] if t.strip()]
    return []


def main() -> int:
    package = sys.argv[1] if len(sys.argv) > 1 else DEFAULT_PACKAGE
    output = run_dumpsys()
    alarms = parse_alarms(output, package)
    history = parse_fire_history(output, package)

    print(f"Package: {package}")
    print(f"Scheduled alarms: {len(alarms)}")
    print("-" * 60)

    if not alarms:
        print("No alarms registered with the system.")
        print("After a reboot this is expected until BootReceiver reschedules them.")
        print("Open the app, or send the boot broadcast manually:")
        print(
            f"  adb shell am broadcast -a android.intent.action.BOOT_COMPLETED "
            f"-n {package}/.receivers.BootReceiver"
        )
    else:
        for n, a in enumerate(alarms, 1):
            print(f"[{n}] {a['kind']}  id={a['id']}")
            print(f"     section : {a['section']}")
            print(f"     trigger : {a['trigger'] or '-'}")
            print(f"     tag     : {a['tag'] or '-'}")
            print(f"     target  : {a['operation'] or '-'}")

    print("-" * 60)
    if history:
        print(f"Recent fires (App Alarm history): {len(history)}")
        for entry in history:
            print(f"  {entry}")
    else:
        print("Recent fires (App Alarm history): none")

    return 0


if __name__ == "__main__":
    sys.exit(main())
