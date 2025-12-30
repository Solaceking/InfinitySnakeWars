package com.solaceking.snakewars

import com.badlogic.gdx.ApplicationAdapter
import com.badlogic.gdx.Gdx
import com.badlogic.gdx.graphics.GL20
import com.badlogic.gdx.graphics.OrthographicCamera
import com.badlogic.gdx.graphics.g2d.BitmapFont
import com.badlogic.gdx.graphics.g2d.SpriteBatch
import com.badlogic.gdx.graphics.glutils.ShapeRenderer
import com.solaceking.snakewars.game.GameScreen
import com.solaceking.snakewars.utils.Constants

/**
 * Main game class - Entry point for Snake Wars
 * Uses LibGDX framework for cross-platform 2D game development
 */
class SnakeWarsGame : ApplicationAdapter() {
    
    // Rendering components
    lateinit var batch: SpriteBatch
    lateinit var shapeRenderer: ShapeRenderer
    lateinit var font: BitmapFont
    lateinit var camera: OrthographicCamera
    
    // Game screen
    private lateinit var gameScreen: GameScreen
    
    override fun create() {
        // Initialize rendering tools
        batch = SpriteBatch()
        shapeRenderer = ShapeRenderer()
        font = BitmapFont()
        
        // Set up camera (for viewport management)
        camera = OrthographicCamera()
        camera.setToOrtho(false, Constants.VIEWPORT_WIDTH, Constants.VIEWPORT_HEIGHT)
        
        // Initialize game screen
        gameScreen = GameScreen(this)
        
        Gdx.app.log("SnakeWars", "Game initialized successfully")
    }
    
    override fun render() {
        // Clear screen
        Gdx.gl.glClearColor(0.1f, 0.1f, 0.15f, 1f)
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT)
        
        // Calculate delta time (time since last frame)
        val deltaTime = Gdx.graphics.deltaTime
        
        // Update and render game
        gameScreen.update(deltaTime)
        gameScreen.render()
    }
    
    override fun resize(width: Int, height: Int) {
        // Update viewport when screen size changes
        camera.setToOrtho(false, width.toFloat(), height.toFloat())
        gameScreen.resize(width, height)
    }
    
    override fun dispose() {
        // Clean up resources
        batch.dispose()
        shapeRenderer.dispose()
        font.dispose()
        gameScreen.dispose()
    }
}
