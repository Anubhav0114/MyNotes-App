package com.example.notesapp.utils

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.notesapp.R
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.makeramen.roundedimageview.RoundedImageView


class NoteBottomSheetFragment : BottomSheetDialogFragment() {
    var selectedColor = "#171C26"
    private lateinit var fNote1 : FrameLayout
    private lateinit var fNote2 : FrameLayout
    private lateinit var fNote3 : FrameLayout
    private lateinit var fNote4 : FrameLayout
    private lateinit var fNote5 : FrameLayout
    private lateinit var fNote6 : FrameLayout
    private lateinit var layoutImage : LinearLayout
    private lateinit var layoutWebLink : LinearLayout
    private lateinit var layoutDelete : LinearLayout



   private lateinit var img_note_1 : ImageView
   private lateinit var img_note_2 : ImageView
   private lateinit var img_note_3 : ImageView
   private lateinit var img_note_4 : ImageView
   private lateinit var img_note_5 : ImageView
   private lateinit var img_note_6 : ImageView




    companion object{
        var noteId = -1
        fun newInstance(id : Int):NoteBottomSheetFragment{
            val args = Bundle()
            val fragment = NoteBottomSheetFragment()
            fragment.arguments = args
            noteId = id
            return fragment
        }
    }

    @SuppressLint("RestrictedApi")
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog, style)

        val view = LayoutInflater.from(context).inflate(R.layout.fragment_notes_bottom_sheet , null)
        dialog.setContentView(view)

        val param = (view.parent as View).layoutParams as CoordinatorLayout.LayoutParams

        val behavior = param.behavior

        if (behavior is BottomSheetBehavior<*>){
            behavior.setBottomSheetCallback(object : BottomSheetBehavior.BottomSheetCallback(){
                override fun onSlide(bottomSheet: View, slideOffset: Float) {
                }

                override fun onStateChanged(bottomSheet: View, newState: Int) {
                    var state = ""

                    when(newState){
                        BottomSheetBehavior.STATE_DRAGGING ->{
                            state = "DRAGGING"
                        }

                        BottomSheetBehavior.STATE_SETTLING ->{
                            state = "SETTLING"
                        }
                        BottomSheetBehavior.STATE_EXPANDED ->{
                            state = "EXPANDED"
                        }
                        BottomSheetBehavior.STATE_COLLAPSED -> {
                            state = "COLLAPSED"
                        }
                        BottomSheetBehavior.STATE_HIDDEN ->{
                            state = "HIDDEN"
                            dismiss()
                            behavior.state = BottomSheetBehavior.STATE_COLLAPSED
                        }

                    }
                }

            })
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_notes_bottom_sheet , container , false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        fNote1 = requireView().findViewById(R.id.fNote1)
        fNote2 = requireView().findViewById(R.id.fNote2)
        fNote3 = requireView().findViewById(R.id.fNote3)
        fNote4 = requireView().findViewById(R.id.fNote4)
        fNote5 = requireView().findViewById(R.id.fNote5)
        fNote6 = requireView().findViewById(R.id.fNote6)
        layoutImage = requireView().findViewById(R.id.layoutImage)
        layoutWebLink = requireView().findViewById(R.id.layoutWebLink)
        layoutDelete = requireView().findViewById(R.id.layoutDelete)


        img_note_1 = requireView().findViewById(R.id.img_note_1)
        img_note_2 = requireView().findViewById(R.id.img_note_2)
        img_note_3 = requireView().findViewById(R.id.img_note_3)
        img_note_4 = requireView().findViewById(R.id.img_note_4)
        img_note_5 = requireView().findViewById(R.id.img_note_5)
        img_note_6 = requireView().findViewById(R.id.img_note_6)


        if (noteId > 0){
            layoutDelete.visibility = View.VISIBLE
        }else{
            layoutDelete.visibility = View.GONE
        }

        setListener()
    }



    private fun setListener (){

        fNote1.setOnClickListener {
            img_note_1.setImageResource(R.drawable.ic_tick)
            img_note_2.setImageResource(0)
            img_note_3.setImageResource(0)
            img_note_4.setImageResource(0)
            img_note_5.setImageResource(0)
            img_note_6.setImageResource(0)
            selectedColor = "#4e33ff"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Blue")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
        fNote2.setOnClickListener {
            img_note_1.setImageResource(0)
            img_note_2.setImageResource(R.drawable.ic_tick)
            img_note_3.setImageResource(0)
            img_note_4.setImageResource(0)
            img_note_5.setImageResource(0)
            img_note_6.setImageResource(0)
            selectedColor = "#ffd633"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Yellow")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)

        }

        fNote3.setOnClickListener {
            img_note_1.setImageResource(0)
            img_note_2.setImageResource(0)
            img_note_3.setImageResource(R.drawable.ic_tick)
            img_note_4.setImageResource(0)
            img_note_5.setImageResource(0)
            img_note_6.setImageResource(0)
            selectedColor = "#ae3b76"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Purple")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
        fNote4.setOnClickListener {
            img_note_1.setImageResource(0)
            img_note_2.setImageResource(0)
            img_note_3.setImageResource(0)
            img_note_4.setImageResource(R.drawable.ic_tick)
            img_note_5.setImageResource(0)
            img_note_6.setImageResource(0)
            selectedColor = "#0aebaf"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Green")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
        fNote5.setOnClickListener {
            img_note_1.setImageResource(0)
            img_note_2.setImageResource(0)
            img_note_3.setImageResource(0)
            img_note_4.setImageResource(0)
            img_note_5.setImageResource(R.drawable.ic_tick)
            img_note_6.setImageResource(0)
            selectedColor = "#ff7746"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Orange")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
        fNote6.setOnClickListener {
            img_note_1.setImageResource(0)
            img_note_2.setImageResource(0)
            img_note_3.setImageResource(0)
            img_note_4.setImageResource(0)
            img_note_5.setImageResource(0)
            img_note_6.setImageResource(R.drawable.ic_tick)
            selectedColor = "#202734"

            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Black")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }

        layoutImage.setOnClickListener{
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Image")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
        layoutWebLink.setOnClickListener{
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "webUrl")
            intent.putExtra("selectedColor", selectedColor)
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
        layoutDelete.setOnClickListener{
            val intent = Intent("bottom_sheet_action")
            intent.putExtra("action", "Delete")
            LocalBroadcastManager.getInstance(requireContext()).sendBroadcast(intent)
        }
    }
}

