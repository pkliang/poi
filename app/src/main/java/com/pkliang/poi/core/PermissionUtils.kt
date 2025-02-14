package com.pkliang.poi.core

import android.Manifest
import android.app.Dialog
import android.content.DialogInterface
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.fragment.app.DialogFragment
import com.pkliang.poi.R

/**
 * Requests the fine location permission. If a rationale with an additional explanation should
 * be shown to the user, displays a dialog that triggers the request.
 */
fun AppCompatActivity.requestPermission(requestId: Int, permission: String, finishActivity: Boolean) {
    if (ActivityCompat.shouldShowRequestPermissionRationale(this, permission)) {
        // Display a dialog with rationale.
        RationaleDialog.newInstance(requestId, finishActivity)
            .show(supportFragmentManager, "dialog")
    } else {
        // Location permission has not been granted yet, request it.
        ActivityCompat.requestPermissions(this, arrayOf(permission), requestId)
    }
}

/**
 * Checks if the result contains a [PackageManager.PERMISSION_GRANTED] result for a
 * permission from a runtime permissions request.
 */
fun isPermissionGranted(
    grantPermissions: Array<String>,
    grantResults: IntArray,
    permission: String
): Boolean {
    for (i in grantPermissions.indices) {
        if (permission == grantPermissions[i]) {
            return grantResults[i] == PackageManager.PERMISSION_GRANTED
        }
    }
    return false
}

/**
 * A dialog that displays a permission denied message.
 */
class PermissionDeniedDialog : DialogFragment() {

    private var mFinishActivity = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        mFinishActivity = arguments!!.getBoolean(ARGUMENT_FINISH_ACTIVITY)

        return AlertDialog.Builder(requireActivity())
            .setMessage(R.string.location_permission_denied)
            .setPositiveButton(android.R.string.ok, null)
            .create()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (mFinishActivity) {
            Toast.makeText(
                activity, R.string.permission_required_toast,
                Toast.LENGTH_SHORT
            ).show()
            requireActivity().finish()
        }
    }

    companion object {

        private const val ARGUMENT_FINISH_ACTIVITY = "finish"

        /**
         * Creates a new instance of this dialog and optionally finishes the calling Activity
         * when the 'Ok' button is clicked.
         */
        fun newInstance(finishActivity: Boolean): PermissionDeniedDialog {
            val arguments = Bundle()
            arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity)

            val dialog = PermissionDeniedDialog()
            dialog.arguments = arguments
            return dialog
        }
    }
}

/**
 * A dialog that explains the use of the location permission and requests the necessary
 * permission.
 *
 *
 * The activity should implement
 * to handle permit or denial of this permission request.
 */
class RationaleDialog : DialogFragment() {

    private var mFinishActivity = false

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val arguments = arguments
        val requestCode = arguments!!.getInt(ARGUMENT_PERMISSION_REQUEST_CODE)
        mFinishActivity = arguments.getBoolean(ARGUMENT_FINISH_ACTIVITY)

        return AlertDialog.Builder(activity!!)
            .setMessage(R.string.permission_rationale_location)
            .setPositiveButton(android.R.string.ok) { dialog, which ->
                // After click on Ok, request the permission.
                ActivityCompat.requestPermissions(
                    activity!!,
                    arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
                    requestCode
                )
                // Do not finish the Activity while requesting permission.
                mFinishActivity = false
            }
            .setNegativeButton(android.R.string.cancel, null)
            .create()
    }

    override fun onDismiss(dialog: DialogInterface?) {
        super.onDismiss(dialog)
        if (mFinishActivity) {
            Toast.makeText(
                activity,
                R.string.permission_required_toast,
                Toast.LENGTH_SHORT
            )
                .show()
            activity!!.finish()
        }
    }

    companion object {

        private const val ARGUMENT_PERMISSION_REQUEST_CODE = "requestCode"

        private const val ARGUMENT_FINISH_ACTIVITY = "finish"

        /**
         * Creates a new instance of a dialog displaying the rationale for the use of the location
         * permission.
         *
         *
         * The permission is requested after clicking 'ok'.
         *
         * @param requestCode Id of the request that is used to request the permission. It is
         * returned to the
         * @param finishActivity Whether the calling Activity should be finished if the dialog is
         * cancelled.
         */
        fun newInstance(requestCode: Int, finishActivity: Boolean): RationaleDialog {
            val arguments = Bundle()
            arguments.putInt(ARGUMENT_PERMISSION_REQUEST_CODE, requestCode)
            arguments.putBoolean(ARGUMENT_FINISH_ACTIVITY, finishActivity)
            val dialog = RationaleDialog()
            dialog.arguments = arguments
            return dialog
        }
    }
}
