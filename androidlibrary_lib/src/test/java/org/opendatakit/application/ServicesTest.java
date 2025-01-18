package org.opendatakit.application;

import android.content.Context;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runners.MethodSorters;
import org.junit.FixMethodOrder;
import org.opendatakit.services.R;
import org.opendatakit.services.application.Services;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ServicesTest {

    private Services services;

    @Rule
    public final ODKServiceTestRule mServiceRule = new ODKServiceTestRule();

    @Before
    public void setUp() throws Exception {
        services = new Services();
        services.onCreate();
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void OnCreate_initializesServices() {
        assertNotNull(services);
    }

    @Test
    public void GetApkDisplayNameResourceId() {
        int apkDisplayNameResourceId = services.getApkDisplayNameResourceId();
        assertEquals(R.string.app_name, apkDisplayNameResourceId);
    }

    @Test
    public void SingletonInstanceInitialization() {
        Context singleton = Services._please_dont_use_getInstance();
        assertNotNull(singleton);
    }

    public class ODKServiceTestRule implements org.junit.rules.TestRule {
        @Override
        public org.junit.runners.model.Statement apply(final org.junit.runners.model.Statement base,
                                                       final org.junit.runner.Description description) {
            return new org.junit.runners.model.Statement() {
                @Override
                public void evaluate() throws Throwable {
                    try {
                        beforeService();
                        base.evaluate();
                    } finally {
                        afterService();
                    }
                }
            };
        }

        protected void beforeService() {
        }

        protected void afterService() {
        }
    }
}
