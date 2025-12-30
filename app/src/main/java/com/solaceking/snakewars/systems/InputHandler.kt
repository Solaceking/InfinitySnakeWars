package com.solaceking.snakewars.systems

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.InputProcessor
import com.solaceking.snakewars.entities.Direction
import com.solaceking.snakewars.entities.Snake
import com.solaceking.snakewars.utils.Constants
import com.solaceking.snakewars.utils.Vector2D
import kotlin.math.abs

/**
 * Input Handler for touch controls
 * Handles swipe gestures and two-finger boost
 */
class InputHandler(private val playerSnake: Snake) : InputProcessor {
    
    // Touch tracking
    private var touchStartPos = Vector2D()
    private var touchStartTime = 0f
    private var isSwiping = false
    
    // Boost tracking
    private var fingerCount = 0
    
    /**
     * Update input state (called every frame)
     */
    fun update(deltaTime: Float) {
        // Update boost based on finger count
        playerSnake.setBoost(fingerCount >= 2)
    }
    
    override fun touchDown(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        fingerCount++
        
        if (pointer == 0) {  // First finger
            touchStartPos.set(screenX.toFloat(), screenY.toFloat())
            touchStartTime = 0f
            isSwiping = true
        }
        
        return true
    }
    
    override fun touchUp(screenX: Int, screenY: Int, pointer: Int, button: Int): Boolean {
        fingerCount = maxOf(0, fingerCount - 1)
        
        if (pointer == 0 && isSwiping) {
            // Process swipe on release
            val endPos = Vector2D(screenX.toFloat(), screenY.toFloat())
            processSwipe(touchStartPos, endPos)
            isSwiping = false
        }
        
        return true
    }
    
    override fun touchDragged(screenX: Int, screenY: Int, pointer: Int): Boolean {
        // Could add real-time swipe detection here if needed
        return true
    }
    
    /**
     * Process swipe gesture and convert to direction
     */
    private fun processSwipe(start: Vector2D, end: Vector2D) {
        val dx = end.x - start.x
        val dy = end.y - start.y
        val distance = start.distanceTo(end)
        
        // Check if swipe is long enough
        if (distance < Constants.MIN_SWIPE_DISTANCE) {
            return
        }
        
        // Determine primary direction
        val direction = if (abs(dx) > abs(dy)) {
            // Horizontal swipe
            if (dx > 0) Direction.RIGHT else Direction.LEFT
        } else {
            // Vertical swipe (note: screen Y is inverted)
            if (dy > 0) Direction.DOWN else Direction.UP
        }
        
        // Apply direction to snake
        playerSnake.changeDirection(direction)
        
        Gdx.app.log("InputHandler", "Swipe detected: $direction")
    }
    
    // Unused InputProcessor methods
    override fun keyDown(keycode: Int): Boolean = false
    override fun keyUp(keycode: Int): Boolean = false
    override fun keyTyped(character: Char): Boolean = false
    override fun mouseMoved(screenX: Int, screenY: Int): Boolean = false
    override fun scrolled(amountX: Float, amountY: Float): Boolean = false
}
