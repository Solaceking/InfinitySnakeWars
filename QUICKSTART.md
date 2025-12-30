# 🚀 Snake Wars - Quick Start Guide

## For Developers

### Prerequisites
- Android Studio Arctic Fox or newer
- JDK 11 or higher
- Android SDK (API 24-34)
- Git

### Setup (5 minutes)

#### 1. Clone the repository
```bash
git clone https://github.com/Solaceking/InfinitySnakeWars.git
cd InfinitySnakeWars
```

#### 2. Open in Android Studio
```
File → Open → Select InfinitySnakeWars directory
Wait for Gradle sync to complete
```

#### 3. Run on device or emulator
```
1. Connect Android device via USB (enable USB debugging)
   OR start Android emulator

2. Click Run button (▶) in Android Studio
   OR press Shift + F10

3. Select target device from dropdown
```

#### 4. Start playing!
```
The game will install and launch automatically
Swipe to move, use two fingers to boost
```

---

## For Testers

### Installation

#### Option A: Build from source (see above)

#### Option B: Install APK (when available)
```bash
# Download latest APK from releases
# Enable "Install from unknown sources" in Settings
# Open APK file to install
```

### Controls
- **Swipe Up/Down/Left/Right**: Change direction
- **Two Fingers (Hold)**: Activate boost (2x speed)

### Tips
- Eat food (colored circles) to grow
- Avoid walls (red border)
- Avoid other snakes' bodies
- Larger snake wins in head-to-head collision
- You can curl over your own body

### Report Bugs
Open an issue at: https://github.com/Solaceking/InfinitySnakeWars/issues

Include:
- Device model
- Android version
- Steps to reproduce
- Screenshot/video if possible

---

## For Contributors

### Branch Strategy
```bash
# Main branches:
master        → Production-ready code
develop       → Integration branch
feature/*     → New features
bugfix/*      → Bug fixes
hotfix/*      → Urgent production fixes
```

### Contribution Workflow
```bash
# 1. Fork the repository
# 2. Create feature branch
git checkout -b feature/awesome-feature

# 3. Make changes and commit
git add .
git commit -m "feat: add awesome feature"

# 4. Push to your fork
git push origin feature/awesome-feature

# 5. Open Pull Request on GitHub
```

### Commit Message Format
```
<type>: <description>

<optional body>

<optional footer>
```

**Types**:
- `feat`: New feature
- `fix`: Bug fix
- `docs`: Documentation changes
- `style`: Code style changes (formatting, etc.)
- `refactor`: Code refactoring
- `perf`: Performance improvements
- `test`: Adding tests
- `chore`: Build process or tooling changes

**Examples**:
```bash
feat: add particle effects to death explosion
fix: resolve collision detection edge case
docs: update installation instructions
perf: optimize spatial grid collision detection
```

---

## Project Structure

```
InfinitySnakeWars/
├── app/
│   ├── src/
│   │   ├── main/
│   │   │   ├── java/com/solaceking/snakewars/
│   │   │   │   ├── SnakeWarsGame.kt       # Main game class
│   │   │   │   ├── MainActivity.kt        # Android entry point
│   │   │   │   ├── entities/              # Game objects
│   │   │   │   │   ├── Snake.kt
│   │   │   │   │   └── Food.kt
│   │   │   │   ├── systems/               # Game systems
│   │   │   │   │   ├── CollisionDetector.kt
│   │   │   │   │   ├── FoodManager.kt
│   │   │   │   │   └── InputHandler.kt
│   │   │   │   ├── game/                  # Game screens
│   │   │   │   │   └── GameScreen.kt
│   │   │   │   ├── ui/                    # UI components
│   │   │   │   │   └── HUD.kt
│   │   │   │   └── utils/                 # Utilities
│   │   │   │       ├── Constants.kt
│   │   │   │       └── Vector2D.kt
│   │   │   ├── AndroidManifest.xml
│   │   │   └── res/                       # Resources
│   ├── build.gradle.kts                   # App build config
├── build.gradle.kts                       # Project build config
├── settings.gradle.kts
├── README.md                              # Project overview
├── DEVELOPMENT.md                         # Development guide
└── QUICKSTART.md                          # This file
```

---

## Key Files to Know

### Game Logic
- `SnakeWarsGame.kt`: Main game initialization and render loop
- `GameScreen.kt`: Core gameplay logic, update loop
- `Snake.kt`: Snake entity with movement, growth, collision
- `Food.kt`: Food entity with types and values

### Systems
- `CollisionDetector.kt`: Spatial grid collision detection
- `FoodManager.kt`: Food spawning and density management
- `InputHandler.kt`: Touch input processing

### Configuration
- `Constants.kt`: All game parameters (speed, size, arena, etc.)
- `AndroidManifest.xml`: App permissions and configuration
- `build.gradle.kts`: Dependencies and build settings

---

## Common Tasks

### Adjust Game Speed
```kotlin
// Edit: app/src/main/java/com/solaceking/snakewars/utils/Constants.kt

const val SNAKE_BASE_SPEED = 200f  // Change to 250f for faster
const val SNAKE_BOOST_MULTIPLIER = 2f  // Change to 3f for faster boost
```

### Change Arena Size
```kotlin
// Edit: Constants.kt

const val ARENA_WIDTH = 6000f  // Make smaller for faster games
const val ARENA_HEIGHT = 6000f
```

### Add New Food Type
```kotlin
// Edit: app/src/main/java/com/solaceking/snakewars/entities/Food.kt

enum class FoodType {
    // ... existing types ...
    
    // Add new type:
    PIZZA("Pizza", 3, 50, Color.ORANGE, 2)
}
```

### Change Colors
```kotlin
// Edit: Constants.kt

object Colors {
    val SNAKE_PLAYER = floatArrayOf(0.2f, 0.8f, 0.3f, 1f)  // RGBA
    val FOOD_BASIC = floatArrayOf(0.8f, 0.4f, 0.2f, 1f)
    // etc.
}
```

---

## Troubleshooting

### Build Errors

**Problem**: `Could not resolve com.badlogicgames.gdx:gdx:1.12.1`  
**Solution**: Check internet connection, sync Gradle again

**Problem**: `Execution failed for task ':app:compileDebugKotlin'`  
**Solution**: Clean project: `Build → Clean Project`, then rebuild

**Problem**: `Installation failed: INSTALL_FAILED_INSUFFICIENT_STORAGE`  
**Solution**: Free up space on device or emulator

### Runtime Errors

**Problem**: App crashes on launch  
**Solution**: Check Logcat for stack trace, look for `AndroidRuntime: FATAL EXCEPTION`

**Problem**: Touch input not working  
**Solution**: Ensure device/emulator supports multi-touch, check InputHandler logs

**Problem**: Low FPS  
**Solution**: Test on physical device (emulator is slower), check for debug builds

### Development Issues

**Problem**: Code changes not taking effect  
**Solution**: Rebuild project: `Build → Rebuild Project`

**Problem**: Gradle sync fails  
**Solution**: Update Gradle version in `gradle-wrapper.properties`

**Problem**: Git push rejected  
**Solution**: Pull latest changes first: `git pull origin master`, resolve conflicts

---

## Performance Tips

### For Development
- Use physical device for accurate testing (emulator is slower)
- Enable "Developer Options" on device for USB debugging
- Use Android Profiler to identify bottlenecks
- Profile on mid-range devices, not just flagships

### For Optimization
- Avoid object allocation in game loop (use object pooling)
- Minimize GC pressure (reuse objects where possible)
- Use ProGuard for release builds (shrinks code)
- Test on target API levels (24 minimum)

---

## Need Help?

### Documentation
- Project README: [README.md](README.md)
- Development Guide: [DEVELOPMENT.md](DEVELOPMENT.md)
- LibGDX Wiki: https://libgdx.com/wiki/

### Community
- GitHub Issues: https://github.com/Solaceking/InfinitySnakeWars/issues
- LibGDX Discord: https://discord.gg/libgdx
- Stack Overflow: Tag questions with `libgdx` and `android`

### Contact
- Repository: https://github.com/Solaceking/InfinitySnakeWars
- Developer: Solaceking

---

**Happy coding! 🐍⚔️**
