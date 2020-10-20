package com.miniproject.bookapp.fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Patterns
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.miniproject.bookapp.R
import com.miniproject.bookapp.model.User
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView


class ProfileFragment : Fragment() {

    lateinit var txtname: TextView
    lateinit var profile_image: CircleImageView
    lateinit var set_profile: TextView
    lateinit var etfull_name: TextInputEditText
    lateinit var etemail: TextInputEditText
    lateinit var etphone_no: TextInputEditText
    lateinit var etpassword: TextInputEditText
    lateinit var btnUpdate: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var fauth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore
    lateinit var storageReference: StorageReference
    lateinit var user: User
    lateinit var uid: String

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_profile, container, false)

        init(view)

        progressLayout.visibility = View.VISIBLE

        uid = fauth.currentUser?.uid.toString()

        fstore.collection("users").document(uid).get()
            .addOnSuccessListener { documentSnapshot ->
                val data = documentSnapshot.data
                if (data != null) {
                    user = User(
                        data["name"].toString(),
                        fauth.currentUser?.email,
                        data["phoneNo"].toString(),
                        data["password"].toString()
                    )
                    setProfileData()
                    progressLayout.visibility = View.GONE
                }
            }

        btnUpdate.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            updateData()
            progressLayout.visibility = View.GONE
        }

        set_profile.setOnClickListener {
            //open gallery
            val opengalleryintent =
                Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            startActivityForResult(opengalleryintent, 1000)
            progressLayout.visibility = View.VISIBLE
        }

        return view
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 1000) {
            if (resultCode == Activity.RESULT_OK) {
                if (data != null) {
                    val profileuri = data.data
                    if (profileuri != null) {
                        uploadImageToFireBase(profileuri)
                    }
                }
            }
        }
    }


    private fun uploadImageToFireBase(uri: Uri) {
        val fileref =
            storageReference.child("users/" + fauth.getCurrentUser()?.uid + "/profile.jpg")
        fileref.putFile(uri).addOnSuccessListener {
            fileref.downloadUrl.addOnSuccessListener {
                Picasso.get().load(uri).into(profile_image)
                progressLayout.visibility = View.GONE
                Toast.makeText(activity, "Image Uploaded!", Toast.LENGTH_SHORT).show()
            }
        }.addOnFailureListener {
            Toast.makeText(activity, it.message, Toast.LENGTH_SHORT).show()
        }
    }


    private fun init(view: View) {
        profile_image = view.findViewById(R.id.profile_image)
        set_profile = view.findViewById(R.id.set_profile)
        txtname = view.findViewById(R.id.txtname)
        etfull_name = view.findViewById(R.id.etfull_name)
        etemail = view.findViewById(R.id.etemail)
        etphone_no = view.findViewById(R.id.etphone_no)
        etpassword = view.findViewById(R.id.etpassword)
        btnUpdate = view.findViewById(R.id.btnUpdate)
        progressLayout = view.findViewById(R.id.progressLayout)
        fauth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()
        storageReference = FirebaseStorage.getInstance().reference

    }

    fun setProfileData() {
        txtname.text = user.name
        etfull_name.setText(user.name)
        etemail.setText(user.emailId)
        etphone_no.setText(user.phoneNo)
        etpassword.setText(user.password)
        val profileRef = storageReference.child(
            "users/" + fauth.getCurrentUser()?.getUid().toString() + "/profile.jpg"
        )
        profileRef.downloadUrl
            .addOnSuccessListener { uri -> Picasso.get().load(uri).into(profile_image) }
    }

    private fun updateData() {
        val name = etfull_name.getText().toString()
        val email = etemail.getText().toString()
        val phoneno = etphone_no.getText().toString()
        val password = etpassword.getText().toString()
        if (name.isNotEmpty() && email.isNotEmpty() && phoneno.isNotEmpty() && password.isNotEmpty()) {
            if (phoneno.length == 10 && Patterns.PHONE.matcher(phoneno).matches()) {
                if (user.emailId.equals(email) && user.password.equals(password) && user.name.equals(
                        name
                    ) && user.phoneNo.equals(phoneno)
                ) {
                    progressLayout.visibility = View.GONE
                    Toast.makeText(activity, "No Changes To Update", Toast.LENGTH_SHORT)
                        .show()
                } else {
                    if (!user.emailId.equals(email)) {
                        fauth.currentUser?.updateEmail(email)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                fstore.collection("users").document(uid).update("emailId", email)
                                user.emailId = fauth.currentUser?.email
                                Toast.makeText(
                                    activity,
                                    "Data Updated!!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                setProfileData()
                            } else {
                                Toast.makeText(
                                    activity,
                                    task.exception?.message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                    if (!user.password.equals(password)) {
                        fauth.currentUser?.updatePassword(password)?.addOnCompleteListener { task ->
                            if (task.isSuccessful) {
                                fstore.collection("users").document(uid)
                                    .update("password", password)
                                user.password = password
                                Toast.makeText(
                                    activity,
                                    "Data Updated!!",
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                                setProfileData()
                            } else {
                                Toast.makeText(
                                    activity,
                                    task.exception?.message,
                                    Toast.LENGTH_SHORT
                                )
                                    .show()
                            }
                        }
                    }
                    if (!user.name.equals(name)) {
                        fstore.collection("users").document(uid).update("name", name)
                        user.name = name
                        Toast.makeText(
                            activity,
                            "Data Updated!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        setProfileData()
                    }

                    if (!user.phoneNo.equals(phoneno)) {
                        fstore.collection("users").document(uid).update("phoneNo", phoneno)
                        user.phoneNo = phoneno
                        Toast.makeText(
                            activity,
                            "Data Updated!!",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        setProfileData()
                    }
                }
            } else {
                progressLayout.visibility = View.GONE
                etphone_no.error = "Invalid"
                Toast.makeText(
                    activity,
                    "Invalid Phone No",
                    Toast.LENGTH_SHORT
                ).show()
            }
        } else {
            progressLayout.visibility = View.GONE
            showError(etfull_name)
            showError(etemail)
            showError(etphone_no)
            showError(etpassword)
        }
    }

    private fun showError(inputField: EditText) {
        if (inputField.text.isEmpty()) {
            inputField.error = "Please fill out this field"
        }
    }
}