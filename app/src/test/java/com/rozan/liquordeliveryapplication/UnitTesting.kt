package com.rozan.liquordeliveryapplication

import com.rozan.liquordeliveryapplication.api.ServiceBuilder
import com.rozan.liquordeliveryapplication.entity.Cart
import com.rozan.liquordeliveryapplication.entity.Carts
import com.rozan.liquordeliveryapplication.entity.Users
import com.rozan.liquordeliveryapplication.repository.AilaRepository
import com.rozan.liquordeliveryapplication.repository.CartRepository
import com.rozan.liquordeliveryapplication.repository.UserRepository
import kotlinx.coroutines.runBlocking
import org.junit.Assert
import org.junit.Test

class UnitTesting {
    private lateinit var userRepository: UserRepository
    private lateinit var cartRepository: CartRepository
    private lateinit var ailaRepository: AilaRepository

    @Test
    fun checkSignUp()= runBlocking {
        val user= Users(firstName = "Salina",
            lastName = "Shrestha",
            dob = "10-2-2000",
            username = "salina10",
            email = "salina@gmail.com",
            password = "salina12")
        userRepository= UserRepository()
        val response=userRepository.registerUser(user)
        val expectedResult=true
        val actualResult=response.success
        Assert.assertEquals(expectedResult,actualResult)
    }

    @Test
    fun checkLogin()= runBlocking {
        userRepository= UserRepository()
        val response=userRepository.checkUser("salina10","salina12")
        val expectedResult=true
        val actualResult=response.success
        Assert.assertEquals(expectedResult,actualResult)
    }

    @Test
    fun addToCart() = runBlocking {
        userRepository = UserRepository()
        cartRepository = CartRepository()
        val cart =
            Carts(ailaQty = 2,userId = "602e47d46a2835449c4ce3d0")
        ServiceBuilder.token ="Bearer " + userRepository.checkUser("sankar12","sankar").token
        val expectedResult = true
        val actualResult = cartRepository.addToCart("60660f4d8a3fc619e005105a",cart).success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun getCart() = runBlocking {
        userRepository = UserRepository()
        cartRepository = CartRepository()
        ServiceBuilder.token ="Bearer " + userRepository.checkUser("sankar12","sankar").token
        ServiceBuilder.userId ="602e47d46a2835449c4ce3d0"
        val response = cartRepository.getCart()
        val expectedResult = true
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }

    @Test
    fun deleteCart() = runBlocking {
        userRepository = UserRepository()
        cartRepository = CartRepository()
        ServiceBuilder.token ="Bearer " + userRepository.checkUser("sankar12","sankar").token
        val expectedResult = true
        val response = cartRepository.deleteCart("607b0c6ca4ee7514c8148919")
        val actualResult = response.success
        Assert.assertEquals(expectedResult, actualResult)
    }
}