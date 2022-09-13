package cn.kt.android.util

import java.lang.NullPointerException
import java.lang.reflect.Proxy


// 被观察者接口
open interface Observable {
    fun addObserver(observer: Observer?)
    fun deleteObserver(observer: Observer?)
    fun notifyObservers(info: String?)
}

// 被观察者
class LibraryObservable : Observable {
    //观察者集合
    private val observers: ArrayList<Observer>?

    init {
        observers = ArrayList()
    }

    @Synchronized
    override fun addObserver(observer: Observer?) {
        if (observer == null) {
            throw NullPointerException()
        }
        if (!observers!!.contains(observer)) {
            observers.add(observer)
        }
    }

    @Synchronized
    override fun deleteObserver(observer: Observer?) {
        if (observer == null) {
            throw NullPointerException()
        }
        observers!!.remove(observer)
    }

    override fun notifyObservers(info: String?) {
        if (observers == null || observers.size <= 0) {
            return
        }
        for (observer in observers) {
            observer.update(info)
        }
    }
}

// 观察者接口
open interface Observer {
    fun update(info: String?)
}

// 观察者
class StudentObserver : Observer {
    override fun update(info: String?) {
        println(info)
    }
}

// ----------------------------------------- 建造者模式 ----------------------------------------------

class Person {
    var name: String? = null  //名字
    var age = 0               //年龄
    var height = 0.0          //身高
    var weight = 0.0          //体重

    constructor() : super() {}

    constructor(builder: Builder) {
        name = builder.name
        age = builder.age
        height = builder.height
        weight = builder.weight
    }

    class Builder {
        var name: String? = null  //名字
        var age = 0               //年龄
        var height = 0.0          //身高
        var weight = 0.0          //体重

        fun setName(name: String?): Builder {
            this.name = name
            return this
        }

        fun setAge(age: Int): Builder {
            this.age = age
            return this
        }

        fun setHeight(height: Double): Builder {
            this.height = height
            return this
        }

        fun setWeight(weight: Double): Builder {
            this.weight = weight
            return this
        }

        fun build(): Person {
            return Person(this)
        }
    }
}
// ------------------------------------- 责任链设计模式   ----------------------------------------------------


// Handler:抽象处理者，声明一个请求的处理方法
open interface Handler {
    fun handleRequest(name: String?, days: Int)
}

// 责任链类
class HandlerChain : Handler {
    private val handlerList: ArrayList<Handler>

    init {
        handlerList = ArrayList()
    }

    fun addHandler(handler: Handler?): HandlerChain {
        handler?.let {
            handlerList.add(it)
        }
        return this
    }

    override fun handleRequest(name: String?, days: Int) {
        for (handler in handlerList) {
            handler.handleRequest(name, days)
        }
    }
}

// 具体处理者
class PMHandler : Handler {
    override fun handleRequest(name: String?, days: Int) {
        if (days <= 3) {
            println("$name，pm has agreed to your leave approval")
        }
    }
}

class DirectorHandler : Handler {
    override fun handleRequest(name: String?, days: Int) {
        if (days in 4..7) {
            println("$name，director has agreed to your leave approval")
        }
    }
}

class MinisterHandler : Handler {
    override fun handleRequest(name: String?, days: Int) {
        if (days in 8..15) {
            println("$name，minister has agreed to your leave approval")
        }
    }
}

// --------------------------- 适配器模式 ------------------------------------------
open interface USB {
    fun isUSB()
}

open interface TypeC {
    fun isTypeC()
}

open class TypeCImpl : TypeC {
    override fun isTypeC() {
        println("typeC 充电口");
    }
}

// 类适配器
class Adapter : TypeCImpl(), USB {
    override fun isUSB() {
        super.isTypeC()
    }
}

// 对象适配器
class AdapterObj(private val typeC: TypeC) : USB {
    override fun isUSB() {
        typeC.isTypeC()
    }
}

// -------------------------------代理模式 ------------------------------------------------
open interface ISinger {
    fun sing()
}

class Singer : ISinger {
    override fun sing() {
        println(" singing ")
    }
}

// 静态代理
class SingerProxy(private val singer: Singer) : ISinger {
    private val mSinger: Singer by lazy {
        singer
    }

    override fun sing() {
        println(" -- static proxy start -- ")
        mSinger.sing()
    }
}

// 动态代理，通过反射在运行时候生成代理对象的
class DynamicProxy {

    private val mSinger: Singer by lazy {
        Singer()
    }

    fun getProxy(): Any? {
        return Proxy.newProxyInstance(
            Singer::class.java.classLoader,
            mSinger.javaClass.interfaces
        ) { proxy, method, args ->
            println(" -- dynamic proxy start -- ")
            method!!.invoke(mSinger, *(args ?: arrayOfNulls<Any>(0)))
        }
    }
}

// ----------------------------- 策略模式 ------------------------------------------

open interface IStrategy {
    fun doAction()
}

class TweenAnimation : IStrategy {
    override fun doAction() {
        println(" -- 补间动画 -- ")
    }
}

class FrameAnimation : IStrategy {
    override fun doAction() {
        println(" -- 逐帧动画 -- ")
    }
}

class ValueAnimator : IStrategy {
    override fun doAction() {
        println(" -- 属性动画 -- ")
    }
}

class AnimatorContext {

    private var strategy: IStrategy? = null

    fun setStrategy(strategy: IStrategy?) {
        this.strategy = strategy
    }

    fun doAction() {
        strategy?.doAction()
    }
}

// -------------------------------装饰模式 ------------------------------------------------
open interface Component {
    fun operate()
}

class ConcreteComponent : Component {
    override fun operate() {
        println(" -- ConcreteComponent operate -- ")
    }
}

abstract class Decoration : Component {
    private var component: Component? = null

    fun setComponent(component: Component?) {
        this.component = component
    }

    override fun operate() {
        component?.operate()
    }
}

class ConcreteComponentA : Decoration() {
    override fun operate() {
        println(" -- ConcreteComponentA operate -- ")
        super.operate()
    }
}

class ConcreteComponentB : Decoration() {
    override fun operate() {
        println(" -- ConcreteComponentB operate -- ")
        super.operate()
    }
}

// ----------------------------------- 工厂模式 ----------------------------------------------------
abstract class ThreadPool {
    fun execute() {
        println(" -- 线程池 --  ")
    }
}

class FixThreadPool : ThreadPool() {
    fun fixThreadExecute() {
        println(" -- 可重用固定线程池 --  ")
    }
}

class SingleThreadPool : ThreadPool() {
    fun singleThreadExecute() {
        println(" -- 单线程化线程池 --  ")
    }
}

// 简单工厂模式
class Factory {
    fun createThreadPool(type: String?): ThreadPool? {
        when (type) {
            "fix" -> return FixThreadPool()
            "single" -> return SingleThreadPool()
        }
        return null
    }
}

// 工厂方法模式
open interface IFactoryPool {
    fun createThreadPool(): ThreadPool
}

class FixPoolFactory : IFactoryPool {
    override fun createThreadPool(): ThreadPool {
        return FixThreadPool()
    }
}

class SinglePoolFactory : IFactoryPool {
    override fun createThreadPool(): ThreadPool {
        return SingleThreadPool()
    }
}

// 抽象工厂模式
open interface IThreadPool {
    fun createThreadPool()
}

class CachedThreadPool : IThreadPool {
    override fun createThreadPool() {
        println(" -- 可缓存线程池 create --  ")
    }
}

class ScheduledThreadPool : IThreadPool {
    override fun createThreadPool() {
        println(" -- 周期性线程池 create --  ")
    }
}

open interface IExecutor {
    fun execute()
}

class CachedThreadExecute : IExecutor {
    override fun execute() {
        println(" -- 可缓存线程池 execute --  ")
    }
}

class ScheduledThreadExecute : IExecutor {
    override fun execute() {
        println(" -- 周期性线程池 execute --  ")
    }
}

abstract class AbstractFactory {
    abstract fun createThreadPool(type: String?): IThreadPool?
    abstract fun createExecute(type: String?): IExecutor?
}

class ThreadPoolFactory : AbstractFactory() {
    override fun createThreadPool(type: String?): IThreadPool? {
        when (type) {
            "cached" -> return CachedThreadPool()
            "scheduled" -> return ScheduledThreadPool()
        }
        return null
    }

    override fun createExecute(type: String?): IExecutor? {
        return null
    }
}

class ExecutorFactory : AbstractFactory() {
    override fun createThreadPool(type: String?): IThreadPool? {

        return null
    }

    override fun createExecute(type: String?): IExecutor? {
        when (type) {
            "cached" -> return CachedThreadExecute()
            "scheduled" -> return ScheduledThreadExecute()
        }
        return null
    }
}

object FactoryProducer {
    fun getFactory(type: String): AbstractFactory? {
        when (type) {
            "cached" -> return ThreadPoolFactory()
            "scheduled" -> return ExecutorFactory()
        }
        return null
    }
}


fun main() {

    val studentA = StudentObserver()
    val studentB = StudentObserver()
    //被观察者图书馆
    val library = LibraryObservable()
    //studentA 和 studentB 在图书馆登记
    library.addObserver(studentA)
    library.addObserver(studentB)
    //图书馆有书了通知
    library.notifyObservers("书到了，先到先得")

    val person =
        Person.Builder().setName("jack").setAge(20).setHeight(180.00).setWeight(120.00).build()

    val handlerChain = HandlerChain()
    handlerChain.addHandler(PMHandler()).addHandler(DirectorHandler()).addHandler(MinisterHandler())
    handlerChain.handleRequest("jack", 5)

    SingerProxy(Singer()).sing()

    val iSinger = DynamicProxy().getProxy() as ISinger
    iSinger.sing()

    val context = AnimatorContext()
    val tweenAnimation = TweenAnimation() as IStrategy
    val frameAnimation = FrameAnimation() as IStrategy
    val valueAnimator = ValueAnimator() as IStrategy

    context.setStrategy(tweenAnimation)
    context.doAction()

    context.setStrategy(frameAnimation)
    context.doAction()

    context.setStrategy(valueAnimator)
    context.doAction()

    val component = ConcreteComponent()
    val concreteComponentA = ConcreteComponentA()
    concreteComponentA.setComponent(component);
    concreteComponentA.operate()

    val concreteComponentB = ConcreteComponentB()
    concreteComponentB.setComponent(component);
    concreteComponentB.operate()


    val factory = Factory()
    val fixThreadPool = factory.createThreadPool("fix")
    fixThreadPool?.let {
        val pool = it as FixThreadPool
        pool.fixThreadExecute()
    }

    val singleThreadPool = factory.createThreadPool("single")
    singleThreadPool?.let {
        val pool = it as SingleThreadPool
        pool.singleThreadExecute()
    }

    val fixPoolFactory = FixPoolFactory().createThreadPool() as FixThreadPool
    fixPoolFactory.fixThreadExecute()
    val singlePoolFactory = SinglePoolFactory().createThreadPool() as SingleThreadPool
    singlePoolFactory.singleThreadExecute()

    val threadPoolFactory = FactoryProducer.getFactory("cached")
    threadPoolFactory?.let {
        val pool =  it.createThreadPool("cached") as CachedThreadPool
        pool.createThreadPool()
    }
    val executorFactory = FactoryProducer.getFactory("scheduled")
    executorFactory?.let {
        val pool =  it.createExecute("scheduled") as ScheduledThreadExecute
        pool.execute()
    }
}

