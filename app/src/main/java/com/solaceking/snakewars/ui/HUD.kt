package com.solaceking.snakewars.ui

import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.Color
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.solaceking.snakewars.SnakeWarsGame
import com.solaceking.snakewars.entities.Snake
import com.solaceking.snakewars.utils.Constants

/**
 * Heads-Up Display (HUD)
 * Shows score, length, energy gauge, and other game info
 */
class HUD {
    
    private val hudCamera = OrthographicCamera()
    
    init {
        // HUD uses screen coordinates, not world coordinates
        hudCamera.setToOrtho(false, Gdx.graphics.width.toFloat(), Gdx.graphics.height.toFloat())
    }
    
    /**
     * Render HUD
     */
    fun render(game: SnakeWarsGame, playerSnake: Snake, foodCount: Int) {
        // Use screen coordinates
        game.batch.projectionMatrix = hudCamera.combined
        game.shapeRenderer.projectionMatrix = hudCamera.combined
        
        renderTopBar(game, playerSnake, foodCount)
        renderEnergyBar(game, playerSnake)
    }
    
    /**
     * Render top bar with score, length, etc.
     */
    private fun renderTopBar(game: SnakeWarsGame, snake: Snake, foodCount: Int) {
        game.batch.begin()
        
        val font = game.font
        font.color = Color.WHITE
        font.data.setScale(Constants.HUD_FONT_SCALE)
        
        val padding = Constants.HUD_PADDING
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()
        
        // Left side: Score and Length
        val leftText = "Score: ${snake.score}  Length: ${snake.getLength()}"
        font.draw(game.batch, leftText, padding, screenHeight - padding)
        
        // Right side: Kills and Food
        val rightText = "Kills: ${snake.kills}  Food: $foodCount"
        val rightLayout = font.draw(game.batch, rightText, 0f, screenHeight - padding)
        // Position right-aligned (get width from layout)
        val rightX = screenWidth - rightLayout.width - padding
        font.draw(game.batch, rightText, rightX, screenHeight - padding)
        
        // Center: Survival time
        val minutes = (snake.survivalTime / 60).toInt()
        val seconds = (snake.survivalTime % 60).toInt()
        val timeText = "Time: %d:%02d".format(minutes, seconds)
        val timeLayout = font.draw(game.batch, timeText, 0f, 0f)
        val centerX = (screenWidth - timeLayout.width) / 2
        font.draw(game.batch, timeText, centerX, screenHeight - padding)
        
        game.batch.end()
    }
    
    /**
     * Render energy/boost gauge
     */
    private fun renderEnergyBar(game: SnakeWarsGame, snake: Snake) {
        val padding = Constants.HUD_PADDING
        val screenWidth = Gdx.graphics.width.toFloat()
        val screenHeight = Gdx.graphics.height.toFloat()
        
        // Energy bar dimensions
        val barWidth = 200f
        val barHeight = 20f
        val barX = (screenWidth - barWidth) / 2
        val barY = screenHeight - 60f
        
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Filled)
        
        // Background (empty bar)
        game.shapeRenderer.color = Color(0.3f, 0.3f, 0.3f, 0.8f)
        game.shapeRenderer.rect(barX, barY, barWidth, barHeight)
        
        // Energy fill
        val energyPercent = snake.energy / Constants.ENERGY_MAX
        val fillWidth = barWidth * energyPercent
        
        // Color based on energy level
        val energyColor = when {
            energyPercent > 0.5f -> Color.GREEN
            energyPercent > 0.25f -> Color.YELLOW
            else -> Color.RED
        }
        
        game.shapeRenderer.color = energyColor
        game.shapeRenderer.rect(barX, barY, fillWidth, barHeight)
        
        // Border
        game.shapeRenderer.end()
        game.shapeRenderer.begin(ShapeRenderer.ShapeType.Line)
        game.shapeRenderer.color = Color.WHITE
        game.shapeRenderer.rect(barX, barY, barWidth, barHeight)
        
        game.shapeRenderer.end()
        
        // Label
        game.batch.begin()
        game.font.color = Color.WHITE
        game.font.data.setScale(1.5f)
        val labelText = "Boost: ${energyPercent.toInt() * 100}%"
        game.font.draw(game.batch, labelText, barX, barY - 5f)
        game.batch.end()
    }
    
    /**
     * Update HUD camera on resize
     */
    fun resize(width: Int, height: Int) {
        hudCamera.setToOrtho(false, width.toFloat(), height.toFloat())
        hudCamera.update()
    }
}
