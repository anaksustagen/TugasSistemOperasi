package com.example.digivobatteryanalytics;

import android.os.Binder;
import android.os.IBinder;
import android.os.IInterface;
import android.os.Parcel;
import android.os.RemoteException;

public interface PowerNotifications extends IInterface {
    void noteAudioOff(int i) throws RemoteException;

    void noteAudioOn(int i) throws RemoteException;

    void noteBluetoothOff() throws RemoteException;

    void noteBluetoothOn() throws RemoteException;

    void noteFullWifiLockAcquired(int i) throws RemoteException;

    void noteFullWifiLockReleased(int i) throws RemoteException;

    void noteInputEvent() throws RemoteException;

    void notePhoneDataConnectionState(int i, boolean z) throws RemoteException;

    void notePhoneOff() throws RemoteException;

    void notePhoneOn() throws RemoteException;

    void noteScanWifiLockAcquired(int i) throws RemoteException;

    void noteScanWifiLockReleased(int i) throws RemoteException;

    void noteScreenBrightness(int i) throws RemoteException;

    void noteScreenOff() throws RemoteException;

    void noteScreenOn() throws RemoteException;

    void noteStartGps(int i) throws RemoteException;

    void noteStartMedia(int i, int i2) throws RemoteException;

    void noteStartSensor(int i, int i2) throws RemoteException;

    void noteStartWakelock(int i, String str, int i2) throws RemoteException;

    void noteStopGps(int i) throws RemoteException;

    void noteStopMedia(int i, int i2) throws RemoteException;

    void noteStopSensor(int i, int i2) throws RemoteException;

    void noteStopWakelock(int i, String str, int i2) throws RemoteException;

    void noteSystemMediaCall(int i) throws RemoteException;

    void noteUserActivity(int i, int i2) throws RemoteException;

    void noteVideoOff(int i) throws RemoteException;

    void noteVideoOn(int i) throws RemoteException;

    void noteVideoSize(int i, int i2, int i3, int i4) throws RemoteException;

    void noteWifiMulticastDisabled(int i) throws RemoteException;

    void noteWifiMulticastEnabled(int i) throws RemoteException;

    void noteWifiOff(int i) throws RemoteException;

    void noteWifiOn(int i) throws RemoteException;

    void noteWifiRunning() throws RemoteException;

    void noteWifiStopped() throws RemoteException;

    void recordCurrentLevel(int i) throws RemoteException;

    void setOnBattery(boolean z, int i) throws RemoteException;

    public static abstract class Stub extends Binder implements PowerNotifications {
        private static final String DESCRIPTOR = "com.example.digivobatteryanalytics.PowerNotifications";
        static final int TRANSACTION_noteAudioOff = 36;
        static final int TRANSACTION_noteAudioOn = 35;
        static final int TRANSACTION_noteBluetoothOff = 24;
        static final int TRANSACTION_noteBluetoothOn = 23;
        static final int TRANSACTION_noteFullWifiLockAcquired = 25;
        static final int TRANSACTION_noteFullWifiLockReleased = 26;
        static final int TRANSACTION_noteInputEvent = 14;
        static final int TRANSACTION_notePhoneDataConnectionState = 18;
        static final int TRANSACTION_notePhoneOff = 17;
        static final int TRANSACTION_notePhoneOn = 16;
        static final int TRANSACTION_noteScanWifiLockAcquired = 27;
        static final int TRANSACTION_noteScanWifiLockReleased = 28;
        static final int TRANSACTION_noteScreenBrightness = 7;
        static final int TRANSACTION_noteScreenOff = 13;
        static final int TRANSACTION_noteScreenOn = 12;
        static final int TRANSACTION_noteStartGps = 5;
        static final int TRANSACTION_noteStartMedia = 8;
        static final int TRANSACTION_noteStartSensor = 3;
        static final int TRANSACTION_noteStartWakelock = 1;
        static final int TRANSACTION_noteStopGps = 6;
        static final int TRANSACTION_noteStopMedia = 9;
        static final int TRANSACTION_noteStopSensor = 4;
        static final int TRANSACTION_noteStopWakelock = 2;
        static final int TRANSACTION_noteSystemMediaCall = 11;
        static final int TRANSACTION_noteUserActivity = 15;
        static final int TRANSACTION_noteVideoOff = 34;
        static final int TRANSACTION_noteVideoOn = 33;
        static final int TRANSACTION_noteVideoSize = 10;
        static final int TRANSACTION_noteWifiMulticastDisabled = 30;
        static final int TRANSACTION_noteWifiMulticastEnabled = 29;
        static final int TRANSACTION_noteWifiOff = 20;
        static final int TRANSACTION_noteWifiOn = 19;
        static final int TRANSACTION_noteWifiRunning = 21;
        static final int TRANSACTION_noteWifiStopped = 22;
        static final int TRANSACTION_recordCurrentLevel = 32;
        static final int TRANSACTION_setOnBattery = 31;

        public Stub() {
            attachInterface(this, DESCRIPTOR);
        }

        public static PowerNotifications asInterface(IBinder iBinder) {
            if (iBinder == null) {
                return null;
            }
            IInterface queryLocalInterface = iBinder.queryLocalInterface(DESCRIPTOR);
            if (queryLocalInterface == null || !(queryLocalInterface instanceof PowerNotifications)) {
                return new Proxy(iBinder);
            }
            return (PowerNotifications) queryLocalInterface;
        }

        public IBinder asBinder() {
            return this;
        }

        public boolean onTransact(int i, Parcel parcel, Parcel parcel2, int i2) throws RemoteException {
            boolean z = false;
            switch (i) {
                case 1:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteStartWakelock(parcel.readInt(), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 2:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteStopWakelock(parcel.readInt(), parcel.readString(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 3:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteStartSensor(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteStopSensor /*4*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteStopSensor(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteStartGps /*5*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteStartGps(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteStopGps /*6*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteStopGps(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteScreenBrightness /*7*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteScreenBrightness(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteStartMedia /*8*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteStartMedia(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 9:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteStopMedia(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 10:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteVideoSize(parcel.readInt(), parcel.readInt(), parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteSystemMediaCall /*11*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteSystemMediaCall(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteScreenOn /*12*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteScreenOn();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteScreenOff /*13*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteScreenOff();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteInputEvent /*14*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteInputEvent();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteUserActivity /*15*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteUserActivity(parcel.readInt(), parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_notePhoneOn /*16*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    notePhoneOn();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_notePhoneOff /*17*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    notePhoneOff();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_notePhoneDataConnectionState /*18*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    int readInt = parcel.readInt();
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    notePhoneDataConnectionState(readInt, z);
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteWifiOn /*19*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteWifiOn(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteWifiOff /*20*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteWifiOff(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteWifiRunning /*21*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteWifiRunning();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteWifiStopped /*22*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteWifiStopped();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteBluetoothOn /*23*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteBluetoothOn();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteBluetoothOff /*24*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteBluetoothOff();
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteFullWifiLockAcquired /*25*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteFullWifiLockAcquired(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteFullWifiLockReleased /*26*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteFullWifiLockReleased(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteScanWifiLockAcquired /*27*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteScanWifiLockAcquired(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteScanWifiLockReleased /*28*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteScanWifiLockReleased(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteWifiMulticastEnabled /*29*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteWifiMulticastEnabled(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteWifiMulticastDisabled /*30*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteWifiMulticastDisabled(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_setOnBattery /*31*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    if (parcel.readInt() != 0) {
                        z = true;
                    }
                    setOnBattery(z, parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 32:
                    parcel.enforceInterface(DESCRIPTOR);
                    recordCurrentLevel(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteVideoOn /*33*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteVideoOn(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteVideoOff /*34*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteVideoOff(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteAudioOn /*35*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteAudioOn(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case TRANSACTION_noteAudioOff /*36*/:
                    parcel.enforceInterface(DESCRIPTOR);
                    noteAudioOff(parcel.readInt());
                    parcel2.writeNoException();
                    return true;
                case 1598968902:
                    parcel2.writeString(DESCRIPTOR);
                    return true;
                default:
                    return super.onTransact(i, parcel, parcel2, i2);
            }
        }

        private static class Proxy implements PowerNotifications {
            private IBinder mRemote;

            Proxy(IBinder iBinder) {
                this.mRemote = iBinder;
            }

            public IBinder asBinder() {
                return this.mRemote;
            }

            public String getInterfaceDescriptor() {
                return Stub.DESCRIPTOR;
            }

            public void noteStartWakelock(int i, String str, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeInt(i2);
                    this.mRemote.transact(1, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteStopWakelock(int i, String str, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeString(str);
                    obtain.writeInt(i2);
                    this.mRemote.transact(2, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteStartSensor(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(3, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteStopSensor(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(Stub.TRANSACTION_noteStopSensor, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteStartGps(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteStartGps, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteStopGps(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteStopGps, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteScreenBrightness(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteScreenBrightness, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteStartMedia(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(Stub.TRANSACTION_noteStartMedia, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteStopMedia(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(9, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteVideoSize(int i, int i2, int i3, int i4) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    obtain.writeInt(i3);
                    obtain.writeInt(i4);
                    this.mRemote.transact(10, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteSystemMediaCall(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteSystemMediaCall, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteScreenOn() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_noteScreenOn, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteScreenOff() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_noteScreenOff, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteInputEvent() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_noteInputEvent, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteUserActivity(int i, int i2) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    obtain.writeInt(i2);
                    this.mRemote.transact(Stub.TRANSACTION_noteUserActivity, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void notePhoneOn() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_notePhoneOn, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void notePhoneOff() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_notePhoneOff, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void notePhoneDataConnectionState(int i, boolean z) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    this.mRemote.transact(Stub.TRANSACTION_notePhoneDataConnectionState, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteWifiOn(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteWifiOn, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteWifiOff(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteWifiOff, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteWifiRunning() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_noteWifiRunning, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteWifiStopped() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_noteWifiStopped, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteBluetoothOn() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_noteBluetoothOn, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteBluetoothOff() throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    this.mRemote.transact(Stub.TRANSACTION_noteBluetoothOff, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteFullWifiLockAcquired(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteFullWifiLockAcquired, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteFullWifiLockReleased(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteFullWifiLockReleased, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteScanWifiLockAcquired(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteScanWifiLockAcquired, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteScanWifiLockReleased(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteScanWifiLockReleased, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteWifiMulticastEnabled(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteWifiMulticastEnabled, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteWifiMulticastDisabled(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteWifiMulticastDisabled, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void setOnBattery(boolean z, int i) throws RemoteException {
                int i2 = 0;
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    if (z) {
                        i2 = 1;
                    }
                    obtain.writeInt(i2);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_setOnBattery, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void recordCurrentLevel(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(32, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteVideoOn(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteVideoOn, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteVideoOff(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteVideoOff, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteAudioOn(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteAudioOn, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }

            public void noteAudioOff(int i) throws RemoteException {
                Parcel obtain = Parcel.obtain();
                Parcel obtain2 = Parcel.obtain();
                try {
                    obtain.writeInterfaceToken(Stub.DESCRIPTOR);
                    obtain.writeInt(i);
                    this.mRemote.transact(Stub.TRANSACTION_noteAudioOff, obtain, obtain2, 0);
                    obtain2.readException();
                } finally {
                    obtain2.recycle();
                    obtain.recycle();
                }
            }
        }
    }
}
