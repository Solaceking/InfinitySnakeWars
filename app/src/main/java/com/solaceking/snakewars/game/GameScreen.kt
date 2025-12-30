package com.solaceking.snakewars.game

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.solaceking.snakewars.SnakeWarsGame
import com.solaceking.snakewars.entities.Food
import com.solaceking.snakewars.entities.Snake
import com.solaceking.snakewars.systems.CollisionDetector
import com.solaceking.snakewars.systems.FoodManager
import com.solaceking.snakewars.systems.InputHandler
import com.solaceking.snakewars.ui.HUD
import com.solaceking.snakewars.utils.Constants
import com.solaceking.snakewars.utils.Vector2D
import kotlin.random.Random

/**
 * Main Game Screen
 * Handles game loop, rendering, and game state
 */
class GameScreen(private val game: SnakeWarsGame) {
    
    // Camera follows player
    private val camera = OrthographicCamera()
    
    // Systems
    private val collisionDetector = CollisionDetector()
    private val foodManager = FoodManager()
    private lateinit var inputHandler: InputHandler
    
    // Entities
    private lateinit var playerSnake: Snake
    private val aiSnakes = mutableListOf<Snake>()
    
    // UI
    private val hud = HUD()
    
    // Game state
    private var initialized = false
    
    init {
        setupCamera()
        initializeGame()
    }
    
    /**
     * Set up camera
     */
    private fun setupCamera() {
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT)
        camera.update()
    }
    
    /**
     * Initialize game entities and systems
     */
    private fun initializeGame() {
        // Create player snake at center
        val centerX = Constants.ARENA_WIDTH / 2
        val centerY = Constants.ARENA_HEIGHT / 2
        playerSnake = Snake(centerX, centerY, isPlayer = true, color = Color.GREEN)
        
        // Set up input
        inputHandler = InputHandler(playerSnake)
        Gdx.input.inputProcessor = inputHandler
        
        // Spawn initial food
        repeat(100) {
            foodManager.spawnRandomFood()
        }
        
        // Spawn AI snakes (for MVP - will be replaced with multiplayer later)
        spawnAISnakes(5)
        
        initialized = true
        Gdx.app.log("GameScreen", "Game initialized")
    }
    
    /**
     * Spawn AI snakes at random positions
     */
    private fun spawnAISnakes(count: Int) {
        repeat(count) {
            val x = Random.nextFloat() * Constants.ARENA_WIDTH
            val y = Random.nextFloat() * Constants.ARENA_HEIGHT
            val color = Color(
                Random.nextFloat(),
                Random.nextFloat(),
                Random.nextFloat(),
                1f
            )
            aiSnakes.add(Snake(x, y, isPlayer = false, color = color))
        }
    }
    
    /**
     * Update game state (called every frame)
     */
    fun update(deltaTime: Float) {
        if (!initialized) return
        
        // Update input
        inputHandler.update(deltaTime)
        
        // Update player snake
        playerSnake.update(deltaTime)
        
        // Update AI snakes
        for (snake in aiSnakes) {
            if (snake.alive) {
                updateAISnake(snake, deltaTime)
                snake.update(deltaTime)
            }
        }
        
        // Update food manager
        foodManager.update(deltaTime)
        
        // Check collisions
        checkCollisions()
        
        // Update camera to follow player
        updateCamera()
        
        // Respawn player if dead
        if (!playerSnake.alive) {
            respawnPlayer()
        }
    }
    
    /**
     * Simple AI behavior: move towards nearest food
     */
    private fun updateAISnake(snake: Snake, deltaTime: Float) {
        // Every 2 seconds, pick a random direction or head towards food
        if (Random.nextFloat() < 0.02f) {  // 2% chance per frame at 60fps ≈ once per second
            val nearestFood = findNearestFood(snake.getHeadPosition())
            
            if (nearestFood != null) {
                val head = snake.getHeadPosition()
                val dx = nearestFood.position.x - head.x
                val dy = nearestFood.position.y - head.y
                
                // Pick direction based on larger delta
                val newDirection = if (kotlin.math.abs(dx) > kotlin.math.abs(dy)) {
                    if (dx > 0) com.solaceking.snakewars.entities.Direction.RIGHT 
                    else com.solaceking.snakewars.entities.Direction.LEFT
                } else {
                    if (dy > 0) com.solaceking.snakewars.entities.Direction.UP 
                    else com.solaceking.snakewars.entities.Direction.DOWN
                }
                
                snake.changeDirection(newDirection)
            }
        }
    }
    
    /**
     * Find nearest food to a position
     */
    private fun findNearestFood(position: Vector2D): Food? {
        return foodManager.getFoods()
            .filter { it.alive }
            .minByOrNull { it.position.distanceTo(position) }
    }
    
    /**
     * Check all collisions
     */
    private fun checkCollisions() {
        val allSnakes = listOf(playerSnake) + aiSnakes
        
        // Check wall collisions
        for (snake in allSnakes) {
            if (collisionDetector.checkWallCollision(snake)) {
                handleSnakeDeath(snake, null)
            }
        }
        
        // Check food collisions
        for (snake in allSnakes) {
            if (!snake.alive) continue
            
            val food = collisionDetector.checkFoodCollision(snake, foodManager.getFoods())
            if (food != null) {
                snake.eatFood(food.type.segments, food.type.points)
                foodManager.removeFood(food)
            }
        }
        
        // Check snake-to-snake collisions
        for (snake in allSnakes) {
            if (!snake.alive) continue
            
            val result = collisionDetector.checkSnakeCollisions(snake, allSnakes)
            when (result) {
                is CollisionDetector.CollisionResult.HeadToHead -> {
                    handleHeadToHeadCollision(snake, result.opponent)
                }
                is CollisionDetector.CollisionResult.HitBody -> {
                    handleSnakeDeath(snake, result.bodyOwner)
                }
                else -> {}
            }
        }
    }
    
    /**
     * Handle head-to-head collision (size-based combat)
     */
    private fun handleHeadToHeadCollision(snake1: Snake, snake2: Snake) {
        when {
            snake1.getLength() > snake2.getLength() -> {
                // snake1 wins
                snake1.score += (snake2.score * Constants.SCORE_KILL_HEAD_TO_HEAD).toInt()
                snake1.kills++
                handleSnakeDeath(snake2, snake1)
            }
            snake1.getLength() < snake2.getLength() -> {
                // snake2 wins
                snake2.score += (snake1.score * Constants.SCORE_KILL_HEAD_TO_HEAD).toInt()
                snake2.kills++
                handleSnakeDeath(snake1, snake2)
            }
            else -> {
                // Equal size - both die
                handleSnakeDeath(snake1, null)
                handleSnakeDeath(snake2, null)
            }
        }
    }
    
    /**
     * Handle snake death
     */
    private fun handleSnakeDeath(victim: Snake, killer: Snake?) {
        if (!victim.alive) return
        
        // Spawn food from dead snake
        foodManager.spawnFoodFromDeath(victim)
        
        // Award points to killer if body collision
        if (killer != null && victim !== killer) {
            killer.score += (victim.score * Constants.SCORE_KILL_BODY_COLLISION).toInt()
            killer.kills++
        }
        
        // Kill the snake
        victim.die()
        
        Gdx.app.log("GameScreen", "Snake died. Killer: ${killer != null}")
    }
    
    /**
     * Respawn player at random safe location
     */
    private fun respawnPlayer() {
        val x = Random.nextFloat() * Constants.ARENA_WIDTH
        val y = Random.nextFloat() * Constants.ARENA_HEIGHT
        playerSnake.reset(x, y)
        Gdx.app.log("GameScreen", "Player respawned")
    }
    
    /**
     * Update camera to follow player
     */
    private fun updateCamera() {
        val head = playerSnake.getHeadPosition()
        camera.position.set(head.x, head.y, 0f)
        
        // Keep camera within arena bounds
        camera.position.x = camera.position.x.coerceIn(
            camera.viewportWidth / 2,
            Constants.ARENA_WIDTH - camera.viewportWidth / 2
        )
        camera.position.y = camera.position.y.coerceIn(
            camera.viewportHeight / 2,
            Constants.ARENA_HEIGHT - camera.viewportHeight / 2
        )
        
        camera.update()
    }
    
    /**
     * Render game (called every frame)
     */
    fun render() {
        // Set camera for world rendering
        game.shapeRenderer.projectionMatrix = camera.combined
        game.batch.projectionMatrix = camera.combined
        
        // Render arena background
        renderArena()
        
        // Render food
        renderFood()
        
        // Render snakes
        renderSnakes()
        
        // Render HUD (uses screen coordinates, not world)
        hud.render(game, playerSnake, foodManager.getFoodCount())
    }
    
    /**
     * Render arena (walls and grid)
     */
    private fun renderArena() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        
        // Draw arena border
        game.shapeRenderer.color = Color.RED
        game.shapeRenderer.rect(0f, 0f, Constants.ARENA_WIDTH, Constants.ARENA_HEIGHT)
        
        game.shapeRenderer.end()
    }
    
    /**
     * Render all food
     */
    private fun renderFood() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        
        for (food in foodManager.getFoods()) {
            if (!food.alive) continue
            
            val scale = food.getPulseScale()
            val radius = Constants.FOOD_RADIUS * scale
            
            game.shapeRenderer.color = food.type.color
            game.shapeRenderer.circle(food.position.x, food.position.y, radius)
        }
        
        game.shapeRenderer.end()
    }
    
    /**
     * Render all snakes
     */
    private fun renderSnakes() {
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        
        // Render AI snakes
        for (snake in aiSnakes) {
            if (snake.alive) {
                renderSnake(snake, isPlayer = false)
            }
        }
        
        // Render player snake (on top)
        if (playerSnake.alive) {
            renderSnake(playerSnake, isPlayer = true)
        }
        
        game.shapeRenderer.end()
    }
    
    /**
     * Render a single snake
     */
    private fun renderSnake(snake: Snake, isPlayer: Boolean) {
        val alpha = if (snake.invulnerable) 0.5f else if (isPlayer) 0.8f else 1f
        
        // Render body segments
        for (i in snake.segments.indices.reversed()) {
            val segment = snake.segments[i]
            val isHead = (i == 0)
            
            val radius = if (isHead) Constants.SNAKE_HEAD_RADIUS else Constants.SNAKE_BODY_RADIUS
            
            // Set color with alpha
            val color = snake.color.cpy()
            color.a = alpha
            game.shapeRenderer.color = color
            
            // Draw segment
            game.shapeRenderer.circle(segment.position.x, segment.position.y, radius)
        }
    }
    
    /**
     * Handle window resize
     */
    fun resize(width: Int, height: Int) {
        camera.viewportWidth = width.toFloat()
        camera.viewportHeight = height.toFloat()
        camera.update()
    }
    
    /**
     * Dispose resources
     */
    fun dispose() {
        // Nothing to dispose yet (using shared resources from game)
    }
}
