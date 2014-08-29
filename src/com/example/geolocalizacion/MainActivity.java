package com.example.geolocalizacion;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import android.app.Activity;
import android.content.Context;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import java.io.IOException;


public class MainActivity extends Activity {
	
	private LocationManager locManager;
	private GoogleMap map;
	Geocoder geocoder;
	private Button ubicar;
	private Button buscar;
	private TextView lat;
	private TextView lon;
	private TextView text;
	
	public void buscar (View v){
		text=(TextView) findViewById(R.id.editText3);
		String buscn = text.getText().toString();
		try{
			List<android.location.Address> addreses = geocoder.getFromLocationName(buscn, 1);
			double latm= addreses.get(0).getLatitude();
			double lonm= addreses.get(0).getLongitude();
			map.addMarker(new MarkerOptions().position(new LatLng(latm,lonm)).title("Mi ubicacion actual"));
		}catch(Exception e){
			e.printStackTrace();
			Toast.makeText(getApplicationContext(), "no se ha encontrado la ubicación solicitada", Toast.LENGTH_SHORT).show();
		}
	}
	public void ubicar(View v){
		Toast.makeText(getApplicationContext(), "hola mundo", Toast.LENGTH_SHORT).show();
		//map.addMarker(new MarkerOptions().position(new LatLng(5,5)).title("Mi ubicacion actual"));
		lat = (TextView) findViewById(R.id.editText1);
		lon = (TextView) findViewById(R.id.editText2);
		double latn= Double.parseDouble(lat.getText().toString());
		double lonn= Double.parseDouble(lon.getText().toString());
		map.addMarker(new MarkerOptions().position(new LatLng(latn,lonn)).title("Mi ubicacion actual"));
	}
	//map.addMarker(new MarkerOptions().position(new LatLng(Double.parseDouble(findViewById(R.id.editText1).toString()),Double.parseDouble(findViewById(R.id.editText1).toString()))).title("Mi ubicacion actual"));
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ubicar =(Button) findViewById(R.id.button1);
        buscar = (Button) findViewById(R.id.button2);
        setContentView(R.layout.activity_main);
        if (savedInstanceState == null) {			
			// Create a fragment
			LayoutMenu fragment = new LayoutMenu();
			getFragmentManager().beginTransaction().add(android.R.id.content, fragment,	fragment.getClass().getSimpleName()).commit(); // 
		}
      
        registerLocation();
        map = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();
        //map.addMarker(new MarkerOptions().position(new LatLng(0,0)).title("Mi primer marker"));
    }
    private void initLocation (){
    	locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	Location loc = locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
    	
    	System.out.println(String.valueOf(loc.getLatitude()));
    }
    
    private void getListProviders(){
    	locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	List<String> listaProviders = locManager.getAllProviders();
    	String mejorProvider = locManager.getBestProvider(getBestCriteria(), false);
    	System.out.println(mejorProvider);
    	LocationProvider provider = locManager.getProvider(listaProviders.get(0));
    	System.out.println(provider.getAccuracy());
    	System.out.println(provider.getPowerRequirement());
    	System.out.println(provider.supportsAltitude());
    }
    private boolean isGpsAvailable(){
    	locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	if(!locManager.isProviderEnabled(LocationManager.GPS_PROVIDER))
    		return false;
    	else
    		return true;
    }
    private Criteria getBestCriteria(){
    	Criteria req = new Criteria();
    	req.setAccuracy(Criteria.ACCURACY_FINE);
    	req.setAltitudeRequired(true);
    	return req;
    }
    private void registerLocation(){
    	locManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    	locManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,5000,0,new MyLocationListener());
    	
    }
    private class MyLocationListener implements LocationListener{

		@Override
		public void onLocationChanged(Location location) {
			System.out.println("La posición ha cambiado");
			Location loc =locManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
	    	loc.getLatitude();
	    	map.addMarker(new MarkerOptions().position(new LatLng(loc.getLatitude(),loc.getLongitude())).title("Mi ubicacion actual"));
	    	//Toast.makeText(getApplicationContext(), "la latitud es: "+(double)loc.getLatitude(), Toast.LENGTH_LONG).show();
			
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderEnabled(String provider) {
			// TODO Auto-generated method stub
			
		}

		@Override
		public void onProviderDisabled(String provider) {
			// TODO Auto-generated method stub
			
		}
    	
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
