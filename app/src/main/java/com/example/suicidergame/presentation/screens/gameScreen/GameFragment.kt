package com.example.suicidergame.presentation.screens.gameScreen

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.graphics.RectF
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.ViewTreeObserver
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.content.res.AppCompatResources
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.example.suicidergame.R
import com.example.suicidergame.databinding.FragmentGameBinding
import com.example.suicidergame.presentation.screens.storeFragment.LifePreserves
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class GameFragment : Fragment() {

    private var _binding: FragmentGameBinding? = null
    private val binding get() = _binding!!

    private var score = 0
    private var earnedMoney = 0

    private var swipeInitialX: Float = 0f
    private var swipeInitialY: Float = 0f

    private var initialLifeRingX: Float = 0f
    private var initialLifeRingY: Float = 0f
    private var initialCharacterX: Float = 0f
    private var initialCharacterY: Float = 0f

    private lateinit var spotViewAnimator: ValueAnimator

    private val lifeRingRect = RectF()
    private val spotViewRect = RectF()
    private val viewModel: GameViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGameBinding.inflate(inflater, container, false)
        setTouchEvent()

        setContent()

        setViewRect()

        return binding.root
    }

    private fun setViewRect() {
        with(binding) {
            spotViewRect.set(
                spotView.x,
                spotView.y,
                spotView.x + spotView.width,
                spotView.y + spotView.height
            )

            lifeRingRect.set(
                lifeRingImageView.x,
                lifeRingImageView.y,
                lifeRingImageView.x + lifeRingImageView.width,
                lifeRingImageView.y + lifeRingImageView.height
            )
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.root.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                binding.root.viewTreeObserver.removeOnGlobalLayoutListener(this)
                animateMovingView()
            }
        })
    }

    private fun setContent() {
        with(binding) {
            spotView.setColor(R.color.spotColor)
            lifeRingImageView.setImageDrawable(
                AppCompatResources.getDrawable(
                    requireContext(),
                    LifePreserves.entries[viewModel.getChosenLifePreserver()].id
                )
            )
            textViewScore.text = buildString {
                append(requireContext().getString(R.string.score_label))
                append(score.toString())
            }
            earnedMoney = viewModel.getUserMoney()
            textViewMoney.text = earnedMoney.toString()
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setTouchEvent() {
        with(binding) {
            lifeRingImageView.setOnTouchListener { _, event ->
                when (event.action) {
                    MotionEvent.ACTION_DOWN -> {
                        swipeInitialX = event.rawX
                        swipeInitialY = event.rawY
                        initialLifeRingX = lifeRingImageView.x
                        initialLifeRingY = lifeRingImageView.y
                    }

                    MotionEvent.ACTION_MOVE -> {
                    }

                    MotionEvent.ACTION_UP -> {
                        val swipeXDistance = event.rawX - swipeInitialX
                        val xShiftPerY = swipeXDistance / (swipeInitialY - event.rawY)
                        animateLifeRing(xShiftPerY)
                    }
                }
                true
            }
        }
    }

    private fun animateLifeRing(xShiftPerY: Float) {
        with(binding) {
            val centerY = spotView.y

            val animatorX = ValueAnimator.ofFloat(
                lifeRingImageView.x,
                lifeRingImageView.x + xShiftPerY * (swipeInitialY - centerY)
            )
            val animatorY = ValueAnimator.ofFloat(lifeRingImageView.y, centerY)

            animatorX.addUpdateListener {
                lifeRingImageView.x = it.animatedValue as Float
                lifeRingRect.left = it.animatedValue as Float
                lifeRingRect.right = it.animatedValue as Float + lifeRingImageView.width
            }
            animatorY.addUpdateListener {
                lifeRingImageView.y = it.animatedValue as Float
                lifeRingRect.top = it.animatedValue as Float
                lifeRingRect.bottom = it.animatedValue as Float + lifeRingImageView.height
            }

            val animatorSet = AnimatorSet().apply {
                playTogether(animatorX, animatorY)
                duration = 500
                interpolator =
                    AccelerateDecelerateInterpolator()
            }

            animatorSet.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    stopMovingViewAnimation()
                    makeCharacterJump()
                }
            })
            animatorSet.start()
        }
    }

    private fun checkResult() {
        with(binding) {
            val halfSize = lifeRingImageView.width / 2

            val smallerHalfLeft = lifeRingRect.left + halfSize / 2
            val smallerHalfRight = lifeRingRect.right - halfSize / 2


            if (smallerHalfLeft >= spotViewRect.left && smallerHalfRight <= spotViewRect.right) {
                animateLanding()
                score++
                if (score % 5 == 0) earnedMoney++
                textViewScore.text = buildString {
                    append(requireContext().getString(R.string.score_label))
                    append(score.toString())
                }
                textViewMoney.text = earnedMoney.toString()
            } else {
                showSinkAnimation()
                viewModel.saveScore(score)
                viewModel.addEarnedMoney(earnedMoney)

            }
        }
    }


    private fun stopMovingViewAnimation() {
        spotViewAnimator.pause()
    }

    private fun animateMovingView() {
        with(binding) {
            val startX = 0f
            val endX = root.width - spotView.width.toFloat()

            spotViewAnimator = ValueAnimator.ofFloat(startX, endX).apply {
                duration = 2000
                repeatCount = ValueAnimator.INFINITE
                repeatMode = ValueAnimator.REVERSE

                addUpdateListener {
                    spotView.x = it.animatedValue as Float
                    spotViewRect.left = it.animatedValue as Float
                    spotViewRect.right = it.animatedValue as Float + spotView.width
                    spotViewRect.top = spotView.y
                    spotViewRect.bottom = spotView.y + spotView.height
                }

                start()
            }
        }
    }

    private fun makeCharacterJump() {
        with(binding) {
            initialCharacterX = characterImageView.x
            initialCharacterY = characterImageView.y

            val jumpHeight =
                initialCharacterX - spotViewRect.centerX() - characterImageView.width / 2

            val translateX = PropertyValuesHolder.ofFloat(
                View.X,
                initialCharacterX,
                spotViewRect.centerX() - characterImageView.width / 2
            )
            val translateY = PropertyValuesHolder.ofFloat(
                View.Y,
                initialCharacterY,
                initialCharacterY - jumpHeight,
                spotView.y
            )

            val jumpAnimator =
                ObjectAnimator.ofPropertyValuesHolder(characterImageView, translateX, translateY)
                    .apply {
                duration = 500
                interpolator = AccelerateDecelerateInterpolator()
            }

            jumpAnimator.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    checkResult()
                }
            })
            jumpAnimator.start()
        }
    }

    private fun animateLanding() {
        with(binding) {
            val landingAnimation = ValueAnimator.ofFloat(0f, -50f, 0f)

            landingAnimation.addUpdateListener {
                val value = it.animatedValue as Float
                characterImageView.translationY = value + (lifeRingRect.top - initialCharacterY)
                lifeRingImageView.translationY = value - lifeRingRect.top / 2
            }
            landingAnimation.addListener(object : AnimatorListenerAdapter() {
                override fun onAnimationEnd(animation: Animator) {
                    lifeRingImageView.x = initialLifeRingX
                    lifeRingImageView.y = initialLifeRingY
                    characterImageView.x = initialCharacterX
                    characterImageView.y = initialCharacterY

                    spotViewAnimator.resume()
                }
            })

            landingAnimation.start()

        }
    }

    private fun showSinkAnimation() {
        with(binding) {
            val startY = characterImageView.y
            val downShift = 100f
            val translateY =
                ValueAnimator.ofFloat(characterImageView.y, characterImageView.y + downShift)
                    .apply {
                        duration = 1000
                        addUpdateListener {
                            characterImageView.y = it.animatedValue as Float
                            characterImageView.alpha =
                                1 - ((it.animatedValue as Float) - startY) / 100
                        }
                    }

            AnimatorSet().apply {
                play(translateY)
                addListener(object : AnimatorListenerAdapter() {
                    override fun onAnimationEnd(animation: Animator) {
                        val navOptions = NavOptions.Builder()
                            .setPopUpTo(R.id.menuFragment, false)
                            .build()
                        findNavController().navigate(
                            GameFragmentDirections.actionGameFragmentToGameOverFragment(
                                score
                            ), navOptions
                        )
                    }
                })
                start()
            }
        }
    }


}