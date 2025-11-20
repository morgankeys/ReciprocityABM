# ReciprocityABM

A simple agent-based model from my grad-school days. Based on behavioral-economic theories, agents with varying reciprocity and sensitivity play each other in 2-player response games.

## About the Model

This simulation explores how agents with different social preference parameters interact in game-theoretic scenarios:
- **Rho (ρ)**: Altruism parameter
- **Sigma (σ)**: Behind-ness aversion
- **Theta (θ)**: Reciprocity sensitivity

### Scenarios

- **Sweep games single round**: 1000 agents in 500 games. Repeats for each of 20 game types.
- **Sweep games for 100 rounds**: 100 agents, 500 games per round. Repeats for each of 20 game types.
- **Sweep games for 1000 rounds**: 100 agents, 500 games per round. Repeats for each of 20 game types.

**Note**: These scenarios can produce a large number of text files. Summaries are recorded in the console only.

---

## Quick Start - Run Pre-built Model

Want to quickly try the model without building? Use the pre-built JAR:

### Prerequisites
- Java 8 or later

### Run the Model

```bash
java -jar dist/model.jar
```

The `dist/model.jar` is a self-contained executable JAR (69MB) that includes all dependencies.

**Note**: 
- This is how I compiled the model originally in grad school. See below if you want to build from source using Maven.
- This is a "fat JAR" - you can run it standalone with `-jar` because everything is bundled inside.
- Since this is preserved the original build, it doesn't print to output folder (I added that later). Output will print to the root directory.

---

## Build and Run from Source

### Prerequisites

- **Java JDK 8 or later**
- **Maven 3.6+**
- **Git** (to clone the repository)

### Step 1: Clone the Repository

```bash
git clone https://github.com/yourusername/ReciprocityABM.git
cd ReciprocityABM
```

### Step 2: Verify Dependencies

The project uses Maven with local JAR dependencies in the `lib/` directory.

The `lib/` directory includes:
- `repast.simphony-2.11.0.jar` (69MB - Repast Simphony framework)
- `colt-1.2.0-no_hep.jar` (Colt scientific computing)
- Various Apache Commons, JUNG, and other dependencies (27 additional JARs)

### Step 3: Build the Project

```bash
mvn clean compile
```

This compiles your source code in `src/jSocialPreference/` against the dependencies in `lib/`.

### Step 4: Package the JAR

```bash
mvn package
```

This creates: `target/reciprocity-abm-1.0-SNAPSHOT.jar`

**Important Notes**:
- Your rebuild creates a **"thin JAR"** (~10KB) containing only your compiled code
- It does **NOT** overwrite `dist/model.jar` (which remains as the reference build)
- The thin JAR's manifest automatically references `lib/` dependencies, so you can run it with `-jar`

### Step 5: Run Your Build

The JAR manifest automatically references the `lib/` directory, so you can run it simply:

```bash
java -jar target/reciprocity-abm-1.0-SNAPSHOT.jar
```

**Comparison of Run Commands**:
- **Pre-built JAR**: `java -jar dist/model.jar` (fat JAR with everything bundled)
- **Your rebuild**: `java -jar target/reciprocity-abm-1.0-SNAPSHOT.jar` (thin JAR with manifest classpath)

**Note**: The rebuilt JAR must remain in the `target/` directory (or maintain its relative position to `lib/`) because the manifest uses relative paths to find dependencies.

---

## Project Structure

```
ReciprocityABM/
├── src/jSocialPreference/      # Your model source code
│   ├── Model.java              # Main model class
│   ├── Agent.java              # Agent implementation
│   ├── Game.java               # Game logic
│   ├── GameType.java           # Game type definitions
│   ├── Report.java             # Reporting functionality
│   └── JSocialPreferenceBuilder.java
├── lib/                        # Dependency JARs (29 files)
│   ├── repast.simphony-2.11.0.jar
│   ├── colt-1.2.0-no_hep.jar
│   └── ... (other dependencies)
├── dist/                       # Pre-built distributions
│   └── model.jar               # Original pre-built JAR
├── Supporting info/            # Documentation and papers
│   └── mkeys_FinalPaper.pdf
└── pom.xml                     # Maven build configuration
```

---

## Dependencies

This project uses:

- **Repast Simphony 2.11.0**: Agent-based modeling framework
- **Colt 1.2.0**: High-performance scientific computing library (`cern.jet.random.*`)
- **JUNG 2.0.1**: Graph library for network visualization
- **Apache Commons**: Various utilities (Collections, IO, Lang, Math)
- Additional libraries for data handling and visualization

All dependencies are managed via Maven with local JAR files in the `lib/` directory.

---

## Running Parameters

The model accepts command-line parameters:

### Normal Run
```bash
java -jar target/reciprocity-abm-1.0-SNAPSHOT.jar \
  normal [numAgents] [numRounds] [gameNumber] \
  [rhoMean] [rhoSD] [sigmaMean] [sigmaSD] [thetaMean] [thetaSD]
```

**Example**:
```bash
java -jar target/reciprocity-abm-1.0-SNAPSHOT.jar \
  normal 1000 100 3 0.424 0.1 0.023 0.1 0.111 0.1
```

### Default Run (No Parameters)
```bash
java -jar target/reciprocity-abm-1.0-SNAPSHOT.jar
```

Uses defaults: 1000 agents, 100 rounds, game type 3

---

## Output Files

When you run the model, it generates three types of output files:
- `Agent History_[timestamp]_game-[num].txt` - Agent parameter evolution over time
- `Toss-Ups_[timestamp]_game-[num].txt` - Game decision records
- `All Rounds_[timestamp]_game-[num].txt` - Per-round outcome summaries

**Output Location**: All files are written to timestamped subdirectories under `output/`
- Example: `output/2025-11-20_12h49m24s/`
- Each run creates a new timestamped directory (down to the second for uniqueness)
- Multiple runs within the same second are extremely unlikely, but if needed, you can add milliseconds
- The `output/` directory is ignored by Git to keep the repository clean

**Note**: Original runs from the final paper are preserved in `Supporting info/Previously run data/`.

---

## Development

### IDE Setup

For IntelliJ IDEA or Eclipse:
1. Import as Maven project
2. Maven will automatically configure the classpath using `pom.xml`
3. Set main class: `jSocialPreference.Model`

### Troubleshooting

**Build fails with "cannot find symbol"**:
- Ensure all JARs are present in the `lib/` directory
- Run `mvn clean` and try again

**"lib/ directory not found"**:
- The `lib/` and `dist/` JARs should be committed to Git (they have exceptions in `.gitignore`)
- If missing, re-clone the repository or check that JARs were pushed to the remote

**Java version issues**:
- Project requires Java 8+
- Check your Java version: `java -version`

**"Will rebuilding overwrite the original model.jar?"**:
- **No!** Maven builds to `target/reciprocity-abm-1.0-SNAPSHOT.jar`
- The original `dist/model.jar` remains unchanged
- Your rebuild is a "thin JAR" (code only) while `dist/model.jar` is a "fat JAR" (includes all dependencies)

**"Can I move the rebuilt JAR to another location?"**:
- The rebuilt JAR uses relative paths in its manifest to find `lib/` dependencies
- If you move the JAR, you must also move `lib/` and maintain the relative path structure
- Alternatively, use `dist/model.jar` (fat JAR) which is completely standalone

---

## License

See [LICENSE](LICENSE) file for details.

## References

For more details on the model theory and implementation, see:
- `Supporting info/mkeys_FinalPaper.pdf`

## Contact

For questions or issues with dependencies, please open a GitHub issue.
