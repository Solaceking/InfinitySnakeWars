package com.solaceking.snakewars.utils

import kotlin.math.sqrt

/**
 * 2D Vector for position, velocity, and direction
 */
data class Vector2D(var x: Float = 0f, var y: Float = 0f) {
    
    /**
     * Calculate distance to another vector
     */
    fun distanceTo(other: Vector2D): Float {
        val dx = x - other.x
        val dy = y - other.y
        return sqrt(dx * dx + dy * dy)
    }
    
    /**
     * Get magnitude (length) of vector
     */
    fun magnitude(): Float {
        return sqrt(x * x + y * y)
    }
    
    /**
     * Normalize to unit vector (length = 1)
     */
    fun normalized(): Vector2D {
        val mag = magnitude()
        return if (mag > 0) {
            Vector2D(x / mag, y / mag)
        } else {
            Vector2D(0f, 0f)
        }
    }
    
    /**
     * Add another vector
     */
    fun add(other: Vector2D): Vector2D {
        return Vector2D(x + other.x, y + other.y)
    }
    
    /**
     * Subtract another vector
     */
    fun subtract(other: Vector2D): Vector2D {
        return Vector2D(x - other.x, y - other.y)
    }
    
    /**
     * Multiply by scalar
     */
    fun multiply(scalar: Float): Vector2D {
        return Vector2D(x * scalar, y * scalar)
    }
    
    /**
     * Copy this vector
     */
    fun copy(): Vector2D {
        return Vector2D(x, y)
    }
    
    /**
     * Set values
     */
    fun set(newX: Float, newY: Float) {
        x = newX
        y = newY
    }
    
    companion object {
        // Direction constants
        val UP = Vector2D(0f, 1f)
        val DOWN = Vector2D(0f, -1f)
        val LEFT = Vector2D(-1f, 0f)
        val RIGHT = Vector2D(1f, 0f)
        val ZERO = Vector2D(0f, 0f)
    }
}
