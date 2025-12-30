package com.solaceking.snakewars.entities

import com.badlogic.gdx.graphics.Color
import com.solaceking.snakewars.utils.Constants
import com.solaceking.snakewars.utils.Vector2D
import kotlin.math.floor

/**
 * Direction enum for snake movement
 */
enum class Direction {
    UP, DOWN, LEFT, RIGHT;
    
    fun toVector(): Vector2D {
        return when (this) {
            UP -> Vector2D(0f, 1f)
            DOWN -> Vector2D(0f, -1f)
            LEFT -> Vector2D(-1f, 0f)
            RIGHT -> Vector2D(1f, 0f)
        }
    }
    
    fun opposite(): Direction {
        return when (this) {
            UP -> DOWN
            DOWN -> UP
            LEFT -> RIGHT
            RIGHT -> LEFT
        }
    }
}

/**
 * Snake segment - represents one circle in the snake's body
 */
data class SnakeSegment(
    val position: Vector2D,
    var layer: Int = 0  // For Z-axis layering effect
)

/**
 * Main Snake class
 * Handles movement, growth, collision, and rendering
 */
class Snake(
    startX: Float,
    startY: Float,
    val isPlayer: Boolean = true,
    val color: Color = Color.GREEN
) {
    // Snake state
    var alive = true
    var invulnerable = false
    var invulnerabilityTimer = 0f
    
    // Position and movement
    val segments = mutableListOf<SnakeSegment>()
    var direction = Direction.RIGHT
    private var nextDirection = Direction.RIGHT
    
    // Growth system
    private var length: Float = Constants.SNAKE_START_LENGTH.toFloat()
    private val targetLength: Int
        get() = floor(length).toInt()
    
    // Boost system
    var boosting = false
    var energy = Constants.ENERGY_MAX
    
    // Score
    var score = 0
    var kills = 0
    var foodEaten = 0
    var survivalTime = 0f
    
    init {
        // Initialize starting segments
        for (i in 0 until Constants.SNAKE_START_LENGTH) {
            segments.add(
                SnakeSegment(
                    Vector2D(startX - i * Constants.SNAKE_SEGMENT_SIZE, startY)
                )
            )
        }
    }
    
    /**
     * Update snake state (called every frame)
     */
    fun update(deltaTime: Float) {
        if (!alive) return
        
        // Update survival time
        survivalTime += deltaTime
        score += (Constants.SCORE_PER_SECOND_ALIVE * deltaTime).toInt()
        
        // Handle invulnerability
        if (invulnerable) {
            invulnerabilityTimer -= deltaTime
            if (invulnerabilityTimer <= 0) {
                invulnerable = false
            }
        }
        
        // Handle boost energy
        if (boosting && energy > 0) {
            energy -= Constants.ENERGY_DRAIN_RATE * deltaTime
            if (energy <= 0) {
                energy = 0f
                boosting = false
            }
        }
        
        // Apply pending direction change
        if (nextDirection != direction.opposite()) {
            direction = nextDirection
        }
        
        // Calculate movement speed
        val speed = if (boosting) {
            Constants.SNAKE_BASE_SPEED * Constants.SNAKE_BOOST_MULTIPLIER
        } else {
            Constants.SNAKE_BASE_SPEED
        }
        
        // Move snake
        moveSnake(speed * deltaTime)
        
        // Manage segment count (grow if needed)
        manageSegments()
    }
    
    /**
     * Move the snake forward in current direction
     */
    private fun moveSnake(distance: Float) {
        if (segments.isEmpty()) return
        
        // Calculate new head position
        val head = segments[0]
        val directionVector = direction.toVector()
        val newHeadPos = Vector2D(
            head.position.x + directionVector.x * distance,
            head.position.y + directionVector.y * distance
        )
        
        // Add new head segment
        segments.add(0, SnakeSegment(newHeadPos))
        
        // Remove tail segment(s) to maintain length
        while (segments.size > targetLength) {
            segments.removeAt(segments.size - 1)
        }
    }
    
    /**
     * Manage segment count based on growth
     */
    private fun manageSegments() {
        // If we need more segments, add them
        while (segments.size < targetLength) {
            val tail = segments.last()
            segments.add(SnakeSegment(tail.position.copy()))
        }
    }
    
    /**
     * Change direction (called by input handler)
     */
    fun changeDirection(newDirection: Direction) {
        // Prevent 180-degree turns
        if (newDirection != direction.opposite()) {
            nextDirection = newDirection
        }
    }
    
    /**
     * Eat food and grow
     */
    fun eatFood(segmentsToAdd: Int, points: Int) {
        length += length * Constants.SNAKE_GROWTH_RATE * segmentsToAdd
        score += points
        foodEaten++
        
        // Refill energy
        energy = minOf(Constants.ENERGY_MAX, energy + Constants.ENERGY_REFILL_PER_FOOD)
    }
    
    /**
     * Get head position
     */
    fun getHeadPosition(): Vector2D {
        return if (segments.isNotEmpty()) {
            segments[0].position.copy()
        } else {
            Vector2D(0f, 0f)
        }
    }
    
    /**
     * Get current length
     */
    fun getLength(): Int = segments.size
    
    /**
     * Kill this snake
     */
    fun die() {
        alive = false
    }
    
    /**
     * Reset snake (for respawn)
     */
    fun reset(x: Float, y: Float) {
        alive = true
        invulnerable = true
        invulnerabilityTimer = Constants.RESPAWN_INVULNERABILITY_TIME
        
        segments.clear()
        for (i in 0 until Constants.SNAKE_START_LENGTH) {
            segments.add(
                SnakeSegment(
                    Vector2D(x - i * Constants.SNAKE_SEGMENT_SIZE, y)
                )
            )
        }
        
        direction = Direction.RIGHT
        nextDirection = Direction.RIGHT
        length = Constants.SNAKE_START_LENGTH.toFloat()
        energy = Constants.ENERGY_MAX
        boosting = false
        
        // Don't reset score/stats (persist across lives)
    }
    
    /**
     * Set boost state
     */
    fun setBoost(active: Boolean) {
        if (active && energy > 0) {
            boosting = true
        } else {
            boosting = false
        }
    }
}
