package com.miniproject.bookapp.fragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.google.android.material.textfield.TextInputEditText
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.miniproject.bookapp.R
import com.miniproject.bookapp.model.User

class ProfileFragment : Fragment() {

    lateinit var txtname: TextView
    lateinit var etfull_name: TextInputEditText
    lateinit var etemail: TextInputEditText
    lateinit var etphone_no: TextInputEditText
    lateinit var etpassword: TextInputEditText
    lateinit var btnUpdate: Button
    lateinit var progressLayout: RelativeLayout
    lateinit var fauth: FirebaseAuth
    lateinit var fstore: FirebaseFirestore
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
                    progressLayout.visibility = View.GONE
                    setProfile()
                }
            }

        btnUpdate.setOnClickListener {
            progressLayout.visibility = View.VISIBLE
            updateData()
            progressLayout.visibility = View.GONE
        }

        return view
    }


    private fun init(view: View) {
        txtname = view.findViewById(R.id.txtname)
        etfull_name = view.findViewById(R.id.etfull_name)
        etemail = view.findViewById(R.id.etemail)
        etphone_no = view.findViewById(R.id.etphone_no)
        etpassword = view.findViewById(R.id.etpassword)
        btnUpdate = view.findViewById(R.id.btnUpdate)
        progressLayout = view.findViewById(R.id.progressLayout)
        fauth = FirebaseAuth.getInstance()
        fstore = FirebaseFirestore.getInstance()

    }

    fun setProfile() {
        txtname.text = user.name
        etfull_name.setText(user.name)
        etemail.setText(user.emailId)
        etphone_no.setText(user.phoneNo)
        etpassword.setText(user.password)
    }

    private fun updateData() {
        val name = etfull_name.getText().toString()
        val email = etemail.getText().toString()
        val phoneno = etphone_no.getText().toString()
        val password = etpassword.getText().toString()
        if (name.isNotEmpty() && email.isNotEmpty() && phoneno.isNotEmpty() && password.isNotEmpty()) {
            if (phoneno.length == 10) {
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
                                setProfile()
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
                                setProfile()
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
                        setProfile()
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
                        setProfile()
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