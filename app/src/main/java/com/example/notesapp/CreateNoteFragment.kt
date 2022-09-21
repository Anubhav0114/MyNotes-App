package com.example.notesapp

import android.Manifest
import android.app.Activity.RESULT_OK
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.util.Patterns
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.lifecycle.Lifecycle
import androidx.localbroadcastmanager.content.LocalBroadcastManager
import com.example.notesapp.database.NotesDatabase
import com.example.notesapp.entities.Notes
import com.example.notesapp.utils.NoteBottomSheetFragment
import kotlinx.coroutines.launch
import pub.devrel.easypermissions.AppSettingsDialog
import pub.devrel.easypermissions.EasyPermissions
import java.lang.Exception
import java.text.SimpleDateFormat
import java.util.*


private lateinit var etNoteTitle : EditText
private lateinit var etNoteSubTitle : EditText
private lateinit var etNoteDesc : EditText
private lateinit var  colorview : View
private lateinit var imageNote : ImageView
private lateinit var etWebUrl : EditText
private lateinit var btnOk : Button
private lateinit var btnCancel : Button
private lateinit var layoutWeb : LinearLayout
private lateinit var layoutImageNote : RelativeLayout



private const val TAG = "CreateNoteFragment"
class CreateNoteFragment : BaseFragment() , EasyPermissions.PermissionCallbacks , EasyPermissions.RationaleCallbacks{

    var selectedColor = "#171C26"
    var currentDate: String? = null
    private var READ_STORAGE_PERM = 123
    private var REQUEST_CODE_IMAGE  = 450
    private var selectedImagePath = ""
    private var webLink = ""
    private var noteId = -1


    override fun onCreate(savedInstanceState: Bundle?) {
        Log.i(TAG  , "in OnCreateView Function")

        super.onCreate(savedInstanceState)
        noteId = requireArguments().getInt("noteId")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_create_note, container, false)
        Log.i(TAG  , "in OnCreateView Function")
        Log.i(TAG , " The note Id is $noteId")

    }

    companion object {

        fun newInstance() =
            CreateNoteFragment().apply {
                arguments = Bundle().apply {

                }
            }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        Log.i(TAG  , "in OnViewCreated Function")

        super.onViewCreated(view, savedInstanceState)
        colorview  = requireView().findViewById(R.id.colorView)
        imageNote = requireView().findViewById(R.id.imageNote)
        etWebUrl = requireView().findViewById(R.id.et_webUrl)
        btnOk = requireView().findViewById(R.id.btn_Ok)
        btnCancel = requireView().findViewById(R.id.btn_cancel)
       layoutWeb  = requireView().findViewById(R.id.layout_webUrl)
        layoutImageNote =requireView().findViewById(R.id.layout_image_note)





        Log.i(TAG , "Note id $noteId")
        if (noteId > 0 ) {
            launch {
                context?.let {
                    val notes = NotesDatabase.getDatabase(it).noteDao().getSpecificNote(noteId)
                    Log.i(TAG , "Note id not -1 function")
                    etNoteTitle.setText(notes.title)
                    etNoteSubTitle.setText(notes.subTitle)
                    etNoteDesc.setText(notes.noteText)

                    colorview.setBackgroundColor(Color.parseColor(notes.color))

                    if (notes.imgPath != ""){
                        selectedImagePath = notes.imgPath!!
                        layoutImageNote.visibility = View.VISIBLE
                        imageNote.setImageBitmap(BitmapFactory.decodeFile(notes.imgPath))
                        imageNote.visibility = View.VISIBLE
                    }
                    else{
                        layoutImageNote.visibility = View.GONE
                        imageNote.visibility = View.GONE
                    }

                    if (notes.webLink != ""){
                        etWebUrl.setText(notes.webLink)
                        etWebUrl.visibility = View.VISIBLE
                        layoutWeb.visibility = View.VISIBLE
                    }
                    else{
                     layoutWeb.visibility = View.GONE
                     etWebUrl.visibility = View.GONE
                    }
                }
            }
        }

        LocalBroadcastManager.getInstance(requireContext()).registerReceiver(
            BroadcastReceiver, IntentFilter("bottom_sheet_action")
        )

        val sdf = SimpleDateFormat("dd/mm/yyyy hh:mm:ss")
        currentDate = sdf.format(Date())
        colorview.setBackgroundColor(Color.parseColor(selectedColor))

        val tvDate: TextView = requireView().findViewById(R.id.tvDateTime)
        val imgDone: ImageView = requireView().findViewById(R.id.imgDone)
        val imgBack: ImageView = requireView().findViewById(R.id.imgBack)
        val imgMore: ImageView = requireView().findViewById(R.id.imgMore)
        val imgDelete : ImageView = requireView().findViewById(R.id.img_delete)
        etNoteTitle = requireView().findViewById(R.id.et_note_title)
        etNoteSubTitle = requireView().findViewById(R.id.et_note_subtitle)
        etNoteDesc = requireView().findViewById(R.id.et_notes_des)


        tvDate.text = currentDate

        imgDone.setOnClickListener {
            if (noteId > 0){
                updateNote()
            }else{
                saveNote()
            }
        }

        imgBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        imgMore.setOnClickListener {
            var noteBottomSheetFragment = NoteBottomSheetFragment.newInstance(noteId)
            noteBottomSheetFragment.show(
                requireActivity().supportFragmentManager,
                "Note Bottom Sheet Fragment"
            )
        }
        imgDelete.setOnClickListener{

             layoutImageNote.visibility = View.GONE
            selectedImagePath = ""
        }

        btnOk.setOnClickListener {
            if (etWebUrl.text.toString().trim().isNotEmpty()){
                checkUrl()
            }else{
                Toast.makeText(requireContext() , "Url is Required" , Toast.LENGTH_LONG ).show()
            }
        }
        btnCancel.setOnClickListener{
            layoutWeb.visibility = View.GONE
        }
    }

    private fun updateNote() {


        if (etNoteTitle.text.isNullOrBlank()) {
            Toast.makeText(context, "Title can not be Empty", Toast.LENGTH_LONG).show()
        } else if (etNoteSubTitle.text.isNullOrBlank()) {
            Toast.makeText(context, "Subtitle can not be Empty", Toast.LENGTH_LONG).show()
        } else if (etNoteDesc.text.isNullOrBlank()) {
            Toast.makeText(context, "Note Description can not be Empty", Toast.LENGTH_LONG).show()
        } else {

            launch {

                context?.let {

                    val notes = NotesDatabase.getDatabase(it).noteDao().getSpecificNote(noteId)
                    notes.title = etNoteTitle.text.toString()
                    notes.subTitle = etNoteSubTitle.text.toString()
                    notes.noteText = etNoteDesc.text.toString()
                    notes.dateTime = currentDate
                    notes.color = selectedColor
                    notes.imgPath = selectedImagePath
                    notes.webLink = webLink



                    NotesDatabase.getDatabase(it).noteDao().updateNote(notes)
                    etNoteTitle.setText("")
                    etNoteSubTitle.setText("")
                    etNoteDesc.setText("")

                    //imgNote.visibility = View.GONE

                }
                requireActivity().supportFragmentManager.popBackStack()

            }

        }
    }

    private fun saveNote() {

        if (etNoteTitle.text.isNullOrBlank()) {
            Toast.makeText(context, "Title can not be Empty", Toast.LENGTH_LONG).show()
        } else if (etNoteSubTitle.text.isNullOrBlank()) {
            Toast.makeText(context, "Subtitle can not be Empty", Toast.LENGTH_LONG).show()
        } else if (etNoteDesc.text.isNullOrBlank()) {
            Toast.makeText(context, "Note Description can not be Empty", Toast.LENGTH_LONG).show()
        } else {

            launch {
                val notes = Notes()
                notes.title = etNoteTitle.text.toString()
                notes.subTitle = etNoteSubTitle.text.toString()
                notes.noteText = etNoteDesc.text.toString()
                notes.dateTime = currentDate
                notes.color = selectedColor
                notes.imgPath = selectedImagePath
                notes.webLink = webLink

                context?.let {
                    NotesDatabase.getDatabase(it).noteDao().insertNotes(notes)

                    etNoteTitle.setText("")
                    etNoteSubTitle.setText("")
                    etNoteDesc.setText("")

                    //imgNote.visibility = View.GONE

                }
                requireActivity().supportFragmentManager.popBackStack()

            }
        }

    }

    private fun deleteNote (){
        launch {
            context?.let {
                NotesDatabase.getDatabase(it).noteDao().deleteSpecificNote(noteId)
                requireActivity().supportFragmentManager.popBackStack()

            }
        }
    }

    private fun checkUrl (){
        if (Patterns.WEB_URL.matcher(etWebUrl.text.toString()).matches()){
            btnOk.visibility = View.GONE
            btnCancel.visibility = View.GONE
            etWebUrl.isEnabled = false
            webLink = etWebUrl.text.toString()
        }
        else{
            Toast.makeText(requireContext() , "Url is not Valid" , Toast.LENGTH_LONG).show()
        }
    }



    private val BroadcastReceiver: BroadcastReceiver = object : BroadcastReceiver() {
        override fun onReceive(context: Context?, intent: Intent?) {
            val actionColor = intent?.getStringExtra("action")
             selectedColor = intent?.getStringExtra("selectedColor").toString()

            Log.i(TAG , "the action color is $actionColor and selected color is $selectedColor")



            when (actionColor!!) {

                "Blue" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorview.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Yellow" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorview.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Purple" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorview.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Green" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorview.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Orange" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorview.setBackgroundColor(Color.parseColor(selectedColor))
                }
                "Black" -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorview.setBackgroundColor(Color.parseColor(selectedColor))
                }

                "Image" -> {
                    Log.i(TAG , "Create note Fragment")
                    readStorageTask()
                }
                "webUrl" -> {
                    layoutWeb.visibility = View.VISIBLE
                }

                "Delete" -> {
                    deleteNote()
                }


                else -> {
                    selectedColor = intent.getStringExtra("selectedColor")!!
                    colorview.setBackgroundColor(Color.parseColor(selectedColor))
                }

            }


        }

    }

    override fun onDestroy() {
        LocalBroadcastManager.getInstance(requireContext()).unregisterReceiver(BroadcastReceiver)
        super.onDestroy()
    }


    fun hasReadStoragePerm() : Boolean{
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.READ_EXTERNAL_STORAGE )
    }
    fun hasWriteStoragePerm() : Boolean{
        return EasyPermissions.hasPermissions(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE )

    }

    fun readStorageTask (){
        if (hasReadStoragePerm()){
            pickImageFromGallery()
            Toast.makeText(requireContext() , "Permission Granted" , Toast.LENGTH_LONG).show()

        }else{
            Log.i(TAG,"requesting permission in read storage else window")
            EasyPermissions.requestPermissions(
                requireActivity() ,
                getString(R.string.storage_permission_text),
            READ_STORAGE_PERM,
            Manifest.permission.READ_EXTERNAL_STORAGE
            )
        }
    }

    private fun pickImageFromGallery(){
        var intent = Intent(Intent.ACTION_PICK , MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        if (intent.resolveActivity(requireActivity().packageManager)!=null){
            startActivityForResult(intent,REQUEST_CODE_IMAGE)
        }
    }

    private fun getPathFromUri(contentUri : Uri): String {
        var filePath : String? = null
       var cursor = requireActivity().contentResolver.query(contentUri , null ,null ,null,null)
        if (cursor == null){
            filePath = contentUri.path
        }
        else{
            cursor.moveToFirst()
            var index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath!!
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == REQUEST_CODE_IMAGE && resultCode == RESULT_OK){
            if ( data != null){
                var selectedImageUrl = data.data
                if (selectedImageUrl != null){
                    try {
                        var inputStream = requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        var bitmap = BitmapFactory.decodeStream(inputStream)
                        imageNote.setImageBitmap(bitmap)
                        layoutImageNote.visibility = View.VISIBLE
                        imageNote.visibility = View.VISIBLE

                        selectedImagePath = getPathFromUri(selectedImageUrl)
                    }
                    catch (e:Exception){
                        Toast.makeText(requireContext() , e.message , Toast.LENGTH_LONG).show()
                    }

                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        EasyPermissions.onRequestPermissionsResult(requestCode , permissions,grantResults ,requireActivity())
    }

    override fun onPermissionsGranted(requestCode: Int, perms: MutableList<String>) {

    }

    override fun onPermissionsDenied(requestCode: Int, perms: MutableList<String>) {
        if(EasyPermissions.somePermissionPermanentlyDenied(requireActivity(),perms)){
            AppSettingsDialog.Builder(requireActivity()).build().show()
        }
    }

    override fun onRationaleAccepted(requestCode: Int) {
        TODO("Not yet implemented")
    }

    override fun onRationaleDenied(requestCode: Int) {
        TODO("Not yet implemented")
    }
}