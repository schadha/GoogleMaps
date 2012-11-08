package com.example.maps;

import java.util.List;
import java.util.Locale;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.view.MotionEvent;
import android.widget.Toast;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.parse.Parse;
import com.parse.ParseObject;

public class Main extends MapActivity {

	MapView map;
	long start;
	long stop;
	int x;
	GeoPoint touchedPoint;
	int y;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		map = (MapView) findViewById(R.id.mvMain);
		map.setBuiltInZoomControls(true);
		
		Parse.initialize(this,
				"kOZGSvGCAZxPoUarRMmPIiXkssl7ig73QgIqGPzH",
				"T5F0wFraR1kpkxw5FRLoS9Sf9cpPhZ1u7mPRFq6a");


		Touch t = new Touch();
		List<Overlay> overlayList = map.getOverlays();
		overlayList.add(t);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.activity_main, menu);
		return true;
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
		return false;
	}

	class Touch extends Overlay {

		public boolean onTouchEvent(MotionEvent e, MapView m) {
			if (e.getAction() == MotionEvent.ACTION_DOWN) {
				start = e.getEventTime();
				x = (int) e.getX();
				y = (int) e.getY();
				touchedPoint = map.getProjection().fromPixels(x, y);
			}
			if (e.getAction() == MotionEvent.ACTION_UP) {
				stop = e.getEventTime();
			}
			if (stop - start > 2000) {
				AlertDialog.Builder alert = new AlertDialog.Builder(Main.this);
				alert.setTitle("Google Maps Notifier");

				alert.setMessage("Pin-Point current location?");
				alert.setPositiveButton("YES",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								Geocoder geocoder = new Geocoder(
										getBaseContext(), Locale.getDefault());
								try {
//									List<Address> address = geocoder.getFromLocation(
//											touchedPoint.getLatitudeE6() / 1E6,
//											touchedPoint.getLongitudeE6() / 1E6,
//											1);
//									if (address.size() > 0) {
//										String display = "";
//
//										for (int i = 0; i < address.get(0)
//												.getMaxAddressLineIndex(); i++) {
//											display += address.get(0)
//													.getAddressLine(i) + "\n";
//										}
//										Toast t = Toast.makeText(
//												getBaseContext(), display,
//												Toast.LENGTH_LONG);
//										t.show();
//									}
									System.out.println("Testing");
									LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
									Criteria criteria = new Criteria();
									Location location = locationManager.getLastKnownLocation(locationManager.getBestProvider(criteria, false));
									if (location != null)
									{
										Toast t = Toast.makeText(getBaseContext(), "Your Location is: Lattitude: " + location.getLatitude() + " \n Longitude: " + location.getLongitude(), Toast.LENGTH_LONG);
										t.show();
										
										ParseObject VTTester = new ParseObject("VTMaps");
										VTTester.put("name", "Divit");
										VTTester.put("Lattitude", location.getLatitude());
										VTTester.put("Longitude", location.getLongitude());
										VTTester.saveInBackground();
									}
								} catch (Exception e) {
									e.printStackTrace();
								}
							}

						});
				alert.setNegativeButton("NO",
						new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int id) {
								dialog.cancel();
							}

						});

				alert.show();
				return true;
			}
			return false;
		}
	}
}
