package com.glebalekseevjk.core.utils.di

import android.app.Activity
import android.app.Application
import androidx.fragment.app.Fragment

interface Dependencies

typealias DepsMap = Map<Class<out Dependencies>, @JvmSuppressWildcards Dependencies>

interface HasDependencies {
    val depsMap: DepsMap
}

inline fun <reified D : Dependencies> Fragment.findDependencies(): D {
    return findDependenciesByClass(D::class.java)
}

inline fun <reified D : Dependencies> Activity.findDependencies(): D {
    return findDependenciesByClass(D::class.java)
}

@Suppress("UNCHECKED_CAST")
fun <D : Dependencies> Fragment.findDependenciesByClass(clazz: Class<D>): D {
    return parents
        .mapNotNull { it.depsMap[clazz] }
        .firstOrNull() as D?
        ?: throw IllegalStateException("No Dependencies $clazz in ${allParents.joinToString()}")
}


@Suppress("UNCHECKED_CAST")
fun <D : Dependencies> Activity.findDependenciesByClass(clazz: Class<D>): D {
    return parents
        .mapNotNull { it.depsMap[clazz] }
        .firstOrNull() as D?
        ?: throw IllegalStateException("No Dependencies $clazz in ${allParents.joinToString()}")
}

private val Fragment.parents: Iterable<HasDependencies>
    get() = allParents.mapNotNull { it as? HasDependencies }

private val Activity.parents: Iterable<HasDependencies>
    get() = allParents.mapNotNull { it as? HasDependencies }


private val Fragment.allParents: Iterable<Any>
    get() = object : Iterable<Any> {
        override fun iterator() = object : Iterator<Any> {
            private var currentParentFragment: Fragment? = parentFragment
            private var parentActivity: Activity? = activity
            private var parentApplication: Application? = parentActivity?.application

            override fun hasNext() =
                currentParentFragment != null || parentActivity != null || parentApplication != null

            override fun next(): Any {
                currentParentFragment?.let { parent ->
                    currentParentFragment = parent.parentFragment
                    return parent
                }

                parentActivity?.let { parent ->
                    parentActivity = null
                    return parent
                }

                parentApplication?.let { parent ->
                    parentApplication = null
                    return parent
                }

                throw NoSuchElementException()
            }
        }
    }

private val Activity.allParents: Iterable<Any>
    get() = object : Iterable<Any> {
        override fun iterator() = object : Iterator<Any> {
            private var activity: Activity? = this@allParents
            private var parentApplication: Application? = activity?.application

            override fun hasNext() =
                activity != null || parentApplication != null

            override fun next(): Any {
                activity?.let { parent ->
                    activity = null
                    return parent
                }

                parentApplication?.let { parent ->
                    parentApplication = null
                    return parent
                }

                throw NoSuchElementException()
            }
        }
    }