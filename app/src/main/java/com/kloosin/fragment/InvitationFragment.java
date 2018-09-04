package com.kloosin.fragment;

import android.annotation.TargetApi;
import android.content.ContentResolver;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.kloosin.R;
import com.kloosin.activity.MainActivity;
import com.kloosin.utility.chipview.ChipView;
import com.kloosin.utility.chipview.SimpleChipAdapter;
import com.kloosin.utility.chipview.modal.ContactChip;
import com.kloosin.utility.u.AlertInfoHelper;
import com.kloosin.utility.u.PermissionRequestHelper;
import com.kloosin.utility.u.SPHelper;
import com.kloosin.utility.u.TagNameHelper;

import java.util.ArrayList;
import java.util.Collections;


public class InvitationFragment extends Fragment implements View.OnClickListener {

    private View _view;
    private ChipView cvTag;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        handleNavigationBAR();
        if (null == _view)
            _view = inflater.inflate(R.layout.invitation_fragment_layout, container, false);

        initComponent();

        return _view;
    }

    private void initComponent() {

        _view.findViewById(R.id.back_btn).setOnClickListener( this );
        ((TextView) _view.findViewById(R.id.done_btn)).setText("SKIP");
        _view.findViewById(R.id.done_btn).setOnClickListener( this );
        _view.findViewById(R.id.submit).setOnClickListener( this );

        cvTag = (ChipView) _view.findViewById(R.id.cvTag);

        fetchNPopulateContacts();
    }

    @TargetApi(Build.VERSION_CODES.M)
    private void fetchNPopulateContacts() {
        if (PermissionRequestHelper.getInstance().checkPermission( getContext(), PermissionRequestHelper.Permission.READ_CONTACT_PERMISSION)) {
            fetchContacts();
        } else {
            PermissionRequestHelper.getInstance().requestPermission(getContext(), PermissionRequestHelper.Permission.READ_CONTACT_PERMISSION, PermissionRequestHelper.Permission.READ_CONTACT_PERMISSION_CODE, this);
        }
    }

    private void fetchContacts() {
        ((MainActivity) getContext()).getLoader().show();
        new Thread(new Runnable() {
            public void run() {
                ContentResolver cr = getContext().getContentResolver();
                Cursor cursor = cr.query(ContactsContract.Contacts.CONTENT_URI, null, null, null, null);
                if(cursor.moveToFirst())
                {
                    final ArrayList<ContactChip> data = new ArrayList<>();
                    do
                    {
                        String id = cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts._ID));

                        if(Integer.parseInt(cursor.getString(cursor.getColumnIndex(ContactsContract.Contacts.HAS_PHONE_NUMBER))) > 0)
                        {
                            Cursor pCur = cr.query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,null,ContactsContract.CommonDataKinds.Phone.CONTACT_ID +" = ?",new String[]{ id }, null);
                            while (pCur.moveToNext()) {
                                String contactNumber = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
                                String contactName = pCur.getString(pCur.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
                                ContactChip contactChip = new ContactChip();
                                contactChip.setContactName( contactName );
                                contactChip.setContactNumber( contactNumber );
                                data.add( contactChip );
                                break;
                            }
                            pCur.close();
                        }
                    } while (cursor.moveToNext()) ;
                    Collections.sort(data);
                    ((MainActivity) getContext()).getLoader().dismiss();
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            populateContacts(data);
                        }
                    });
                }
            }
        }).start();
    }

    private void populateContacts(ArrayList<ContactChip> data) {
        SimpleChipAdapter adapter = new SimpleChipAdapter(data);
        cvTag.setAdapter(adapter);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.back_btn:
                ((MainActivity) getContext()).onBackPressed();
                break;
            case R.id.done_btn:
                openFeedFragment();
                break;
            case R.id.submit:
                sendInvitationToSelectedContact();
                break;
        }
    }

    private void sendInvitationToSelectedContact() {
        // TODo need to un-comment bellow line
        //SPHelper.getInstance( getContext() ).writeBoolean(TagNameHelper.getInstance().IS_INVITATION_COMPLETED_KEY, true);
        openFeedFragment();
    }

    private void openFeedFragment() {
        // TODo need to comment below line
        SPHelper.getInstance( getContext() ).writeBoolean(TagNameHelper.getInstance().IS_INVITATION_COMPLETED_KEY, true);
        ((MainActivity) getContext()).pushFragment(new FeedFragment(), false);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        try {
            switch (requestCode) {
                case PermissionRequestHelper.Permission.READ_CONTACT_PERMISSION_CODE:
                    boolean _is_all_permission_granted  =   true;
                    for (int index = 0; index < grantResults.length; index++) {
                        if (grantResults[ index ] != PackageManager.PERMISSION_GRANTED) {
                            _is_all_permission_granted  =   false;
                            break;
                        }
                    }
                    if ( _is_all_permission_granted ) {
                        fetchContacts();   // Just do a double check ;)
                    } else {
                        AlertInfoHelper.getInstance().alertIt(getContext(), getString(R.string.error_dialog_heading), getString(R.string.no_permission_msg));
                    }
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void handleNavigationBAR() {
        ((MainActivity) getContext()).findViewById(R.id.bottom_navigation_section).setVisibility(View.GONE);
    }
}
