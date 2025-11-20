# Dependency Setup - ReciprocityABM

I recently used Sonnet 4.5 to help me clean up the repo. Here are notes on how we changed dependency management to a (hopefully) more modern approach using Maven.

### 1. Created `lib/` Directory

A `lib/` directory was created at the project root containing all necessary dependency JARs.

**Location**: `/lib/`

**Contents** (29 JAR files):
- `repast.simphony-2.11.0.jar` (69MB) - All compiled Repast Simphony classes
- `colt-1.2.0-no_hep.jar` - Colt scientific computing library
- `jung-*.jar` (4 files) - JUNG graph library components
- `commons-*.jar` (5 files) - Apache Commons utilities
- `poi-*.jar` (3 files) - Apache POI for Excel/Office support
- Various other dependencies (unit-api, xmlbeans, velocity, etc.)

**Note**: The `repast.simphony-2.11.0.jar` was copied from your existing `model.jar` file, which already contained all the compiled Repast classes.

### 2. Updated `pom.xml`

The Maven POM file was completely rewritten to properly define all dependencies:

#### Key Changes:

1. **Added Repast Simphony as a compiled JAR** - Uses the existing `model.jar` (renamed to `repast.simphony-2.11.0.jar`)
2. **Removed Maven Central Colt dependency** - Replaced with system dependency pointing to bundled version
3. **Added all 29 JAR dependencies as system dependencies** - Each JAR in `lib/` is now properly referenced
4. **Uses only binary JARs** - No source code compilation needed for Repast

### 3. Dependency Coverage

All imports in your source code are now properly covered:

| Package | Source |
|---------|--------|
| `cern.jet.random.*` | `lib/colt-1.2.0-no_hep.jar` |
| `repast.simphony.random.*` | `lib/repast.simphony-2.11.0.jar` |
| `repast.simphony.util.*` | `lib/repast.simphony-2.11.0.jar` |
| `repast.simphony.context.*` | `lib/repast.simphony-2.11.0.jar` |
| `repast.simphony.dataLoader.*` | `lib/repast.simphony-2.11.0.jar` |
| `repast.simphony.space.grid.*` | `lib/repast.simphony-2.11.0.jar` |

### 4. You Can Delete `repast.simphony-development/`

✅ **The `repast.simphony-development/` directory is no longer needed!**

Since we're now using the pre-compiled `repast.simphony-2.11.0.jar` instead of compiling from source:

```bash
rm -rf repast.simphony-development/
```

See `DELETE_INSTRUCTIONS.md` for details.

## Build Instructions

To build the project:

```bash
mvn clean compile
```

To package as JAR:

```bash
mvn clean package
```

The resulting JAR will be in `target/reciprocity-abm-1.0-SNAPSHOT.jar` with the main class set to `jSocialPreference.Model`.

## Why This Approach?

1. **No duplicate Colt dependency** - Uses the version bundled with Repast
2. **All dependencies are local** - No external downloads required
3. **Source code compilation** - Repast Simphony source is compiled as part of the project
4. **Self-contained** - The entire build is reproducible without internet access

## Notes

- The `.gitignore` file excludes `*.jar` files, so the `lib/` directory won't be tracked by Git
- Consider documenting in your README how to set up the `lib/` directory for other developers
- Alternatively, you could commit the lib/ directory by creating a `.gitignore` exception:
  ```
  !lib/*.jar
  ```

## System Dependencies vs. Local Repository

This configuration uses Maven system dependencies (with `<systemPath>`) rather than installing JARs to the local Maven repository. This approach:

✅ **Advantages:**
- Simple setup - just copy JARs to `lib/`
- No Maven install commands needed
- Clear visibility of all dependencies

⚠️ **Disadvantages:**
- System dependencies don't work well with Maven dependency:tree
- Won't be included in Maven assembly plugin by default (requires additional configuration)
- Some IDEs may need additional configuration

If you need to create a standalone JAR with all dependencies, consider using the `maven-assembly-plugin` or `maven-shade-plugin`.

