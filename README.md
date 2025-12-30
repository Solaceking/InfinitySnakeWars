# 🐍 Snake Wars - Infinity Snake Battle Arena

[![Android](https://img.shields.io/badge/Platform-Android-green.svg)](https://developer.android.com)
[![Kotlin](https://img.shields.io/badge/Language-Kotlin-purple.svg)](https://kotlinlang.org)
[![LibGDX](https://img.shields.io/badge/Engine-LibGDX-red.svg)](https://libgdx.com)
[![API](https://img.shields.io/badge/API-24%2B-brightgreen.svg)](https://android-arsenal.com/api?level=24)

A real-time multiplayer snake battle arena with persistent drop-in/drop-out gameplay. Control your snake, eat food to grow, and eliminate opponents in an infinite arena!

## 🎮 Game Features

### Core Mechanics
- **Continuous Movement**: Snake moves forward automatically - swipe to change direction
- **Boost System**: Hold two fingers on screen for 2x speed (consumes energy)
- **Z-Axis Layering**: Curl over your own body with visual depth effect
- **Percentage Growth**: Grow 1% per food eaten (scales with size)
- **Size-Based Combat**: Larger snake wins head-to-head collisions

### Unique Features
- **No Lobbies**: Drop into ongoing matches instantly
- **Persistent Arena**: 6000×6000 pixel arena with up to 100+ players
- **Smart AI**: AI snakes for single-player testing
- **Food Tiers**: 9 different food types with varying values
- **Death Explosions**: 80% of dead snake segments become food

## 🏗️ Architecture

### Technology Stack
- **Language**: Kotlin
- **Game Engine**: LibGDX 1.12.1
- **Min SDK**: Android 7.0 (API 24)
- **Target SDK**: Android 14 (API 34)

### Project Structure
```
snakewars/
├── entities/          # Game objects (Snake, Food)
├── systems/           # Game systems (Collision, FoodManager, Input)
├── game/              # Main game loop (GameScreen)
├── ui/                # UI components (HUD)
├── utils/             # Utilities (Vector2D, Constants)
└── SnakeWarsGame.kt   # Entry point
```

### Key Systems

#### 1. Snake Entity
- Segment-based body with smooth movement
- Energy management for boost mechanic
- Direction change with 180° turn prevention
- Growth system with float precision

#### 2. Collision Detection
- **Spatial Grid**: O(n) complexity using grid partitioning
- **Wall Collision**: Instant death on boundary hit
- **Food Collision**: Circle-circle overlap detection
- **Snake Collision**: Head-to-head and body collision

#### 3. Food Management
- **Density Control**: Maintains 2% arena coverage
- **Tier System**: Basic (70%), Premium (20%), Special (10%)
- **Anti-Clustering**: Prevents food spawning too close
- **Death Conversion**: Spawns food from dead snakes

#### 4. Input Handling
- **Swipe Detection**: Minimum 30px distance threshold
- **Two-Finger Boost**: Simultaneous touch detection
- **Direction Mapping**: Converts swipe to cardinal directions

## 🚀 Getting Started

### Prerequisites
- Android Studio Arctic Fox or newer
- JDK 11+
- Android SDK with API 24-34
- Gradle 8.0+

### Building the Project

1. **Clone the repository**
```bash
git clone https://github.com/Solaceking/InfinitySnakeWars.git
cd InfinitySnakeWars
```

2. **Open in Android Studio**
```bash
# Open project in Android Studio
# File → Open → Select InfinitySnakeWars directory
```

3. **Sync Gradle**
```
# Android Studio will automatically sync Gradle dependencies
# If not, click: File → Sync Project with Gradle Files
```

4. **Run on device/emulator**
```
# Select device from dropdown
# Click Run (▶) button or Shift+F10
```

### Manual Build (Command Line)
```bash
# Debug build
./gradlew assembleDebug

# Release build (requires signing key)
./gradlew assembleRelease

# Install on connected device
./gradlew installDebug
```

## 🎯 Controls

### Touch Controls
- **Swipe Up/Down/Left/Right**: Change snake direction
- **Two Fingers (Hold)**: Activate boost (2x speed, drains energy)
- **Minimum Swipe**: 30 pixels to register

### Gameplay Tips
- Eat food to grow and refill energy
- Boost to chase smaller snakes or escape larger ones
- Curl over your own body for tactical positioning
- Avoid walls and other snakes' bodies
- Win head-to-head by being larger

## 📊 Game Constants

| Setting | Value | Description |
|---------|-------|-------------|
| Arena Size | 6000×6000 px | Playing field dimensions |
| Base Speed | 200 px/s | Normal snake movement speed |
| Boost Multiplier | 2x | Speed increase when boosting |
| Growth Rate | 1% | Size increase per food eaten |
| Energy Drain | 10%/s | Energy consumption during boost |
| Food Density | 2% | Target food coverage of arena |
| Start Length | 5 segments | Initial snake size |
| Invulnerability | 30s | Protection time after respawn |

## 🔧 Configuration

### Edit Constants
Modify game behavior in `utils/Constants.kt`:

```kotlin
object Constants {
    // Snake speed
    const val SNAKE_BASE_SPEED = 200f  // Change movement speed
    
    // Growth rate
    const val SNAKE_GROWTH_RATE = 0.01f  // Change growth per food
    
    // Arena size
    const val ARENA_WIDTH = 6000f  // Adjust arena dimensions
    const val ARENA_HEIGHT = 6000f
    
    // Food spawning
    const val FOOD_DENSITY_TARGET = 0.02f  // Adjust food density
}
```

## 🎨 Visual Design

### Color Palette
- **Background**: Dark blue-gray (#1A1A26)
- **Player Snake**: Green (#33CC33)
- **Enemy Snakes**: Size-coded (green → yellow → red)
- **Food**: Color-coded by type
- **Walls**: Red glow effect

### Rendering
- 60 FPS target
- Circle-based segments
- Smooth movement interpolation
- Particle effects (planned)
- Z-axis depth perception

## 🗺️ Development Roadmap

### ✅ Phase 1: Single-Player MVP (Current)
- [x] Snake movement and controls
- [x] Collision detection system
- [x] Food spawning and management
- [x] Growth system
- [x] Boost mechanic
- [x] Basic AI opponents
- [x] HUD and UI

### 🔄 Phase 2: Multiplayer (In Progress)
- [ ] Firebase Realtime Database integration
- [ ] Player synchronization (30 Hz)
- [ ] Client-side prediction
- [ ] Lag compensation
- [ ] 10-20 player rooms

### 📋 Phase 3: Content & Polish
- [ ] Power-ups (5 types)
- [ ] Premium food tiers
- [ ] Particle effects
- [ ] Sound effects and music
- [ ] Death screen with stats
- [ ] Main menu

### 🚀 Phase 4: Launch
- [ ] Rewarded video ads
- [ ] Cosmetic shop
- [ ] Leaderboards
- [ ] Achievements
- [ ] Beta testing
- [ ] Google Play release

## 🤝 Contributing

Contributions are welcome! Please follow these guidelines:

1. Fork the repository
2. Create a feature branch (`git checkout -b feature/AmazingFeature`)
3. Commit your changes (`git commit -m 'Add some AmazingFeature'`)
4. Push to the branch (`git push origin feature/AmazingFeature`)
5. Open a Pull Request

### Code Style
- Follow Kotlin coding conventions
- Use meaningful variable names
- Comment complex logic
- Keep functions focused and small
- Write unit tests for systems

## 📝 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

## 🙏 Acknowledgments

- **LibGDX**: Amazing cross-platform game framework
- **slither.io**: Inspiration for multiplayer snake gameplay
- **agar.io**: Inspiration for persistent arena mechanics

## 📧 Contact

- **Developer**: Solaceking
- **Repository**: [github.com/Solaceking/InfinitySnakeWars](https://github.com/Solaceking/InfinitySnakeWars)
- **Issues**: [Report bugs or suggest features](https://github.com/Solaceking/InfinitySnakeWars/issues)

---

**Status**: 🟢 Active Development | **Version**: 1.0.0-alpha | **Last Updated**: December 2024
