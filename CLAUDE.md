# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

TuHoraSalud is an Android medicine management app. Users register/login and manage a personal medicine inventory (CRUD with soft-delete). Built with Java, Room (SQLite), and BCrypt password hashing.

- Package: `com.example.apptuhorasalud`
- Module: `AppTuHoraSalud/`
- Min SDK: 24, Target/Compile SDK: 36, Java 11

## Build & Test Commands

```bash
# Build
./gradlew build
./gradlew assembleDebug

# Install on device/emulator
./gradlew installDebug

# Run unit tests
./gradlew test
./gradlew test --tests ExampleUnitTest

# Run instrumented tests (requires device/emulator)
./gradlew connectedAndroidTest

# Clean
./gradlew clean
```

## Architecture

Clean Architecture with four layers (dependencies flow inward only):

```
Presentation (Activities, Adapters)
    → Application (Use Cases)
        → Domain (Interfaces, Models — no framework annotations)
            ← Infrastructure (Room DAOs, Repository impls, Mappers)
```

**Key rule**: Domain models (`User`, `Medicine`) have zero Room annotations. Mappers (`UserMapper`, `MedicineMapper`) convert bidirectionally between domain models and Room entities. Never pass entities up to presentation or use cases.

## Layer Responsibilities

| Layer | Location | Contents |
|-------|----------|----------|
| Presentation | root package | Activities, `MedicineAdapter` |
| Application | `application/useCase/` | `ValidateUser`, `AddUser`, `UpdateUserPassword`, `ValidateIdentity` |
| Domain | `domain/` | `User`, `Medicine` models; `IUserRepository`, `IMedicineRepository` interfaces |
| Infrastructure | `infrastructure/` | Room entities, DAOs, `UsuarioRepositoryImpl`, `MedicineRepositoryImpl`, mappers |

## Async Pattern

All repository methods return `CompletableFuture`. Repositories use a single-threaded `ExecutorService`. Activities chain `.thenAccept()` + `runOnUiThread()`:

```java
useCase.execute(email, password)
    .thenAccept(user -> runOnUiThread(() -> handleResult(user)))
    .exceptionally(e -> { runOnUiThread(() -> showError()); return null; });
```

## Database

- Room database name: `usuarios-db`, version 3
- Migration strategy: `fallbackToDestructiveMigration()` — schema changes destroy data
- Singleton factory: `DatabaseHelper.getInstance(context)` (note: creates a new instance each call; not truly cached)
- `AppDatabase` exposes `usuarioDao()` and `medicineDao()`

**Tables:**
- `users`: id (PK), document, name, lastname, birth_date, email, password (BCrypt hashed)
- `medicines`: id (PK), name, quantity, userId (FK to users, no constraint enforced), isDeleted (0/1)

**Soft-delete**: `MedicineRepositoryImpl.softDeleteMedicine()` sets `isDeleted = true` then calls `updateMedicine()`. DAO queries filter `WHERE isDeleted = 0`.

## Manual Dependency Wiring

No DI framework — activities wire dependencies by hand:

```java
AppDatabase db = DatabaseHelper.getInstance(getApplicationContext());
IUserRepository repo = new UsuarioRepositoryImpl(db.usuarioDao());
ValidateUser useCase = new ValidateUser(repo);
```

## Navigation & Data Passing

Activities pass data via intent extras (no navigation component). Key extras:
- `"idUsuario"` — logged-in user's integer ID
- `"nombreUsuario"` — logged-in user's display name

`ListMedicinesActivity` calls `loadMedicines()` in `onResume()` to refresh after returning from insert/update screens.

## Known Issues to Be Aware Of

- `DatabaseHelper.getInstance()` builds a new Room instance on every call — cache it if performance matters.
- No foreign key constraint between `medicines.userId` and `users.id`.
- No unique index on `users.email` — duplicate emails possible at DB level.
- `lifecycle-viewmodel` and `lifecycle-livedata` are in dependencies but not used.
- `MedicineAdapter` uses `notifyDataSetChanged()` — no DiffUtil.
