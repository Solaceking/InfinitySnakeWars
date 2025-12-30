package com.solaceking.snakewars.utils

/**
 * Game configuration constants
 * All measurements in pixels unless otherwise noted
 */
object Constants {
    
    // Display settings
    const val VIEWPORT_WIDTH = 1080f
    const val VIEWPORT_HEIGHT = 1920f
    const val TARGET_FPS = 60
    
    // Arena settings
    const val ARENA_WIDTH = 6000f  // 300 tiles × 20px
    const val ARENA_HEIGHT = 6000f
    const val ARENA_TILE_SIZE = 20f
    
    // Snake settings
    const val SNAKE_BASE_SPEED = 200f  // pixels per second
    const val SNAKE_BOOST_MULTIPLIER = 2f
    const val SNAKE_HEAD_RADIUS = 10f
    const val SNAKE_BODY_RADIUS = 8f  // Slightly smaller for forgiving collision
    const val SNAKE_SEGMENT_SIZE = 20f
    const val SNAKE_SEGMENT_SPACING = 2f
    const val SNAKE_START_LENGTH = 5
    const val SNAKE_GROWTH_RATE = 0.01f  // 1% growth per food
    
    // Energy/Boost settings
    const val ENERGY_MAX = 100f
    const val ENERGY_DRAIN_RATE = 10f  // % per second during boost
    const val ENERGY_REFILL_PER_FOOD = 20f  // % regained when eating
    
    // Food settings
    const val FOOD_RADIUS = 7f
    const val FOOD_DENSITY_TARGET = 0.02f  // 2% of arena tiles
    const val FOOD_TIER1_SPAWN_RATE = 0.70f  // 70%
    const val FOOD_TIER2_SPAWN_RATE = 0.20f  // 20%
    const val FOOD_TIER3_SPAWN_RATE = 0.10f  // 10%
    
    // Food values (segments, points)
    val FOOD_VALUES = mapOf(
        "APPLE" to Pair(1, 10),
        "BANANA" to Pair(1, 15),
        "GRAPES" to Pair(2, 25),
        "PIZZA" to Pair(3, 50),
        "BURGER" to Pair(2, 40),
        "CAKE" to Pair(4, 75)
    )
    
    // Input settings
    const val MIN_SWIPE_DISTANCE = 30f  // pixels
    const val SWIPE_THRESHOLD_TIME = 0.3f  // seconds
    
    // Collision settings
    const val SPATIAL_GRID_CELL_SIZE = 100  // pixels per grid cell
    
    // UI settings
    const val HUD_PADDING = 20f
    const val HUD_FONT_SCALE = 2f
    
    // Colors (RGBA)
    object Colors {
        val BACKGROUND = floatArrayOf(0.1f, 0.1f, 0.15f, 1f)
        val SNAKE_PLAYER = floatArrayOf(0.2f, 0.8f, 0.3f, 1f)  // Green
        val SNAKE_ENEMY_SMALLER = floatArrayOf(0.3f, 0.8f, 0.3f, 1f)  // Light green
        val SNAKE_ENEMY_SIMILAR = floatArrayOf(0.9f, 0.9f, 0.2f, 1f)  // Yellow
        val SNAKE_ENEMY_LARGER = floatArrayOf(0.9f, 0.2f, 0.2f, 1f)   // Red
        val FOOD_BASIC = floatArrayOf(0.8f, 0.4f, 0.2f, 1f)  // Orange
        val WALL = floatArrayOf(0.8f, 0.1f, 0.1f, 1f)  // Dark red
        val UI_TEXT = floatArrayOf(1f, 1f, 1f, 1f)  // White
    }
    
    // Z-axis layering (visual depth effect)
    const val Z_LAYER_HEIGHT_OFFSET = 4f  // pixels elevation per layer
    const val Z_LAYER_SCALE_FACTOR = 0.03f  // 3% scale increase per layer
    const val Z_MAX_LAYERS = 10
    
    // Death settings
    const val DEATH_FOOD_CONVERSION_RATE = 0.8f  // 80% of segments become food
    const val RESPAWN_INVULNERABILITY_TIME = 30f  // seconds
    const val RESPAWN_SAFE_DISTANCE = 400f  // pixels from nearest snake
    
    // Score settings
    const val SCORE_PER_SECOND_ALIVE = 1
    const val SCORE_KILL_HEAD_TO_HEAD = 0.5f  // 50% of victim's score
    const val SCORE_KILL_BODY_COLLISION = 0.3f  // 30% of victim's score
}
