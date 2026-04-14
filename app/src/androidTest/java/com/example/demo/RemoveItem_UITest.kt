package com.example.demo


import android.view.View
import android.view.ViewGroup
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.closeSoftKeyboard
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.doesNotExist
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withContentDescription
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withParent
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.rules.ActivityScenarioRule
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.filters.LargeTest
import org.hamcrest.Description
import org.hamcrest.Matcher
import org.hamcrest.Matchers.allOf
import org.hamcrest.TypeSafeMatcher
import org.hamcrest.core.IsInstanceOf
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@LargeTest
@RunWith(AndroidJUnit4::class)
class RemoveItem_UITest {

    @Rule
    @JvmField
    var mActivityScenarioRule = ActivityScenarioRule(MainActivity::class.java)

    @Test
    fun removeItem_UITest() {

        // Ingresar el nombre de la nueva tarea
        val editText = onView(
            allOf(
                withId(R.id.editTaskTitle),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    0
                ),
                isDisplayed()
            )
        )
        editText.perform(replaceText("Tarea 1"), closeSoftKeyboard())

        // Utilizar el boton agregar
        val button = onView(
            allOf(
                withId(R.id.btnAddTask), withText("Agregar"),
                childAtPosition(
                    childAtPosition(
                        withId(android.R.id.content),
                        0
                    ),
                    1
                ),
                isDisplayed()
            )
        )
        button.perform(click())

        // Validar que la lista cuenta con un item
        val viewGroup = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.rvTasks),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup.check(matches(isDisplayed()))

        // Utilizar el boton de eliminar
        val imageButton = onView(
            allOf(
                withId(R.id.btnDeleteTask), withContentDescription("Eliminar tarea"),
                childAtPosition(
                    childAtPosition(
                        withId(R.id.rvTasks),
                        0
                    ),
                    2
                ),
                isDisplayed()
            )
        )
        imageButton.perform(click())

        // Validar que la lista no cuenta con ningun item
        val viewGroup2 = onView(
            allOf(
                withParent(
                    allOf(
                        withId(R.id.rvTasks),
                        withParent(IsInstanceOf.instanceOf(android.view.ViewGroup::class.java))
                    )
                ),
                isDisplayed()
            )
        )
        viewGroup2.check(doesNotExist())
    }

    private fun childAtPosition(
        parentMatcher: Matcher<View>, position: Int
    ): Matcher<View> {

        return object : TypeSafeMatcher<View>() {
            override fun describeTo(description: Description) {
                description.appendText("Child at position $position in parent ")
                parentMatcher.describeTo(description)
            }

            public override fun matchesSafely(view: View): Boolean {
                val parent = view.parent
                return parent is ViewGroup && parentMatcher.matches(parent)
                        && view == parent.getChildAt(position)
            }
        }
    }
}
