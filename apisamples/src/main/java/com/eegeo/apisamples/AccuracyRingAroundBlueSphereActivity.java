package com.eegeo.apisamples;

import android.os.Bundle;
import android.os.Handler;

import com.eegeo.mapapi.EegeoApi;
import com.eegeo.mapapi.EegeoMap;
import com.eegeo.mapapi.MapView;
import com.eegeo.mapapi.geometry.LatLng;
import com.eegeo.mapapi.map.OnMapReadyCallback;
import com.eegeo.mapapi.bluesphere.BlueSphere;

public class AccuracyRingAroundBlueSphereActivity extends WrldExampleActivity {

    private MapView m_mapView;
    private EegeoMap m_eegeoMap = null;
    private BlueSphere m_bluesphere = null;
    private boolean m_accuracyRingToggle = false;
    private Handler m_timerHandler = new Handler();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EegeoApi.init(this, getString(R.string.eegeo_api_key));

        setContentView(R.layout.show_accuracy_ring_around_bluesphere_activity);
        m_mapView = (MapView) findViewById(R.id.show_accuracy_ring_bluesphere_mapview);
        m_mapView.onCreate(savedInstanceState);

        m_mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(final EegeoMap map) {
                m_eegeoMap = map;

                final LatLng location = new LatLng(56.459811, -2.977928);
                final float initialAccuracyRingRadius = 5;
                final float alteredAccuracyRingRadius = 10;

                m_bluesphere = m_eegeoMap.getBlueSphere();
                m_bluesphere.setEnabled(true);
                m_bluesphere.setPosition(location);
                m_bluesphere.setAccuracyRingEnabled(true);
                m_bluesphere.setCurrentLocationAccuracy(initialAccuracyRingRadius);

                m_timerHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        if (m_eegeoMap != null) {
                            m_accuracyRingToggle = !m_accuracyRingToggle;
                            float accuracyRadius = m_accuracyRingToggle ? alteredAccuracyRingRadius : initialAccuracyRingRadius;
                            m_bluesphere.setCurrentLocationAccuracy(accuracyRadius);
                            m_timerHandler.postDelayed(this, 2000);
                        }
                    }
                }, 2000);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        m_mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        m_mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (m_eegeoMap != null) {
            m_eegeoMap = null;
        }
        m_mapView.onDestroy();
    }
}

