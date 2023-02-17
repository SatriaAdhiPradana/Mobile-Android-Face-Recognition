package com.javalab.activities;

import java.util.LinkedList;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.LinearLayout.LayoutParams;
import android.widget.TextView;

import com.javalab.models.Contact;
import com.javalab.models.Picture;
import com.javalab.services.ImageService;
import com.javalab.services.PictureService;

public class ContactDetailActivity extends Activity {

	private static String TAG = "Contact Detail Activity";

	private TextView labelContactFirstName;
	private TextView labelContactLastName;
	private Button buttonBack;
	private Button buttonEdit;
	private LinearLayout layoutContactPictures;

	private Contact contact;
	private ImageService imageService;
	private PictureService pictureService;

	private Boolean edited;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.contact_detail_activity);
		Log.i(TAG, "Activity created");

		imageService = new ImageService(this, getWindowManager());
		pictureService = new PictureService(this);

		contact = (Contact) getIntent().getSerializableExtra("contact");
		edited = false;

		getViews();

		initView();

		// view mode by default
		initViewMode();
	}

	private void getViews() {
		labelContactFirstName = (TextView) findViewById(R.id.value_contact_first_name);
		labelContactLastName = (TextView) findViewById(R.id.value_contact_last_name);
		buttonBack = (Button) findViewById(R.id.button_contact_detail_back);
		buttonEdit = (Button) findViewById(R.id.button_contact_detail_edit);
		layoutContactPictures = (LinearLayout) findViewById(R.id.layout_contact_detail_pictures);
	}

	private void initView() {

		// button back click listener
		buttonBack.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {

				Log.i(TAG, "Button back clicked, return to the FaceReco activity");

				if (edited) {
					getIntent().putExtra("contact", contact);
					setResult(RESULT_OK, getIntent());
				}

				else {
					setResult(RESULT_CANCELED);
				}

				finish();
			}
		});

		// button edit click listener
		buttonEdit.setOnClickListener(new View.OnClickListener() {

			@Override
			public void onClick(View arg0) {
				Log.i(TAG, "Button edit clicked, going into editing mode");

			}
		});

		// update labels with contact info
		labelContactFirstName.setText(contact.getFirstName());
		labelContactLastName.setText(contact.getLastName());

		// get images
		int i = 0;
		LinkedList<LinearLayout> rows = new LinkedList<LinearLayout>();

		for (Picture picture : pictureService.getPicturesFromContact(contact)) {

			if (i % 3 == 0) {
				LinearLayout row = new LinearLayout(this);
				row.setOrientation(LinearLayout.HORIZONTAL);
				LayoutParams params = new LayoutParams(LayoutParams.FILL_PARENT, LayoutParams.WRAP_CONTENT);
				params.setMargins(0, 0, 0, 3);
				row.setLayoutParams(params);
				row.setGravity(Gravity.CENTER_HORIZONTAL);
				rows.add(row);
			}

			ImageView image = imageService.getSmallPictureFromLocal(picture.getPictureUri());
			rows.getLast().addView(image);

			i++;
		}

		for (LinearLayout row : rows) {
			layoutContactPictures.addView(row);
		}
	}

	public void initEditingMode() {

		
	}

	public void initViewMode() {

	}
}
