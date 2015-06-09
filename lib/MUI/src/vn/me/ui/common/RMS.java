package vn.me.ui.common;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import javax.microedition.lcdui.Image;
import javax.microedition.rms.RecordStore;

public class RMS {

    public static final String SERVER_LIST_KEY = "server_list";
    public static final String SERVER_LIST_LOG_REG_FILTER_KEY = "server_list_log_reg";

    //#if iWin_OviStore
//#     public static final String OVI_PASSED = "ovi_passed";
    //#endif
    public static byte[] loadRMS(String filename, int recordIndex) {
        RecordStore rec = null;
        byte[] data;
        try {
            rec = RecordStore.openRecordStore(filename, false);
            //System.out.println("rec " + filename);
            data = rec.getRecord(recordIndex);
        } catch (Exception e) {
            return null;
        } finally {
            if (rec != null) {
                try {
                    rec.closeRecordStore();
                } catch (Exception ex) {
                }
            }
        }
        //System.out.println("rec " + data);
        return data;
    }

    public static void saveRMS(String filename, byte[] data) throws Exception {
        RecordStore rec = RecordStore.openRecordStore(filename, true);
        if (rec.getNumRecords() > 0) {
            rec.setRecord(1, data, 0, data.length);
        } else {
            rec.addRecord(data, 0, data.length);
        }
        rec.closeRecordStore();
    }

    public static void saveUTFString(String filename, String s) {
        try {
            saveRMS(filename, s.getBytes("UTF-8"));
        } catch (Exception e) {
        }
    }

    public static String loadUTFString(String filename) {
        byte[] data = loadRMS(filename, 1);
        if (data == null) {
            return null;
        } else {
            try {
                return new String(data, "UTF-8");
            } catch (Exception e) {
                return new String(data);
            }
        }
    }

    public static void saveImage(String imageName, byte[] image) {
        try {
//            //#if DefaultConfiguration
//            System.out.println("ten luu xuong rms: " + "image_" + imageName);
//            //#endif
            saveRMS("image_" + imageName, image);
        } catch (Exception ex) {
//            ex.printStackTrace();
        }

        //        int[] imgRgbData = new int[img.getWidth() * img.getHeight()];
        //        DataOutputStream dos = null;
        //        try {
        //            RecordStore.deleteRecordStore(imageName);
        //        } catch (RecordStoreException ex) {
        //        }
        //        try {
        ////            img.getRGB(imgRgbData, 0, img.getWidth(), 0, 0, img.getWidth(), img.getHeight());
        //            Effects.getRGB(img);
        //            // Write image data to output stream (in order to get
        //            // the record bytes in needed form)
        //            ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //            dos = new DataOutputStream(baos);
        //            //dos.writeUTF(imageName);
        //            dos.writeInt(img.getWidth());
        //            dos.writeInt(img.getHeight());
        //            for (int i = 0; i < imgRgbData.length; i++) {
        //                dos.writeInt(imgRgbData[i]);
        //            }
        //            // Open record store, create if it doesn't exist
        //            byte[] buffer = baos.toByteArray();
        //            RecordStore rec = RecordStore.openRecordStore(imageName, true);
        //            if (rec.getSizeAvailable() < buffer.length) {
        //                throw new Exception("Hết RMS.");
        //            } else {
        //                rec.addRecord(buffer, 0, buffer.length);
        //            }
        //        } catch (Exception ioe) {
        ////            GlobalService.getInstance().reportError("RMS.saveImage", ioe.getMessage());
        //        } finally {
        //            if (dos != null) {
        //                try {
        //                    dos.close();
        //                } catch (Exception ex) {
        //                }
        //        }
        //        }
    }

    public static Image loadImage(String imageName) {
//        //#if DefaultConfiguration
//        System.out.println("ten se load tu rms: " + "image_" + imageName);
//        //#endif
        byte[] data = loadRMS("image_" + imageName, 1);
        if (data == null) {
            return null;
        } else {
            Image img = Image.createImage(data, 0, data.length);
            return img;
        }


//        try {
//            byte[] data;// = loadRMS(imageName);
//            RecordStore rec = RecordStore.openRecordStore(imageName, false);
//            RecordEnumeration re = rec.enumerateRecords(null, null, true);
//            while (re.hasNextElement()) {
//                //int id = re.nextRecordId();
//                data = rec.getRecord(re.nextRecordId());
//                ByteArrayInputStream bais = new ByteArrayInputStream(data);
//                DataInputStream inputStream = new DataInputStream(bais);
//                int w = inputStream.readInt();
//                int h = inputStream.readInt();
//                int remaining = (data.length - 8) / 4;
//                int[] imageData = new int[remaining];
//                for (int k = 0; k < imageData.length; k++) {
//                    imageData[k] = inputStream.readInt();
//                }
//                inputStream.close();
//                rec.closeRecordStore();
//                return Image.createRGBImage(imageData, w, h, true);
//            }
//        } catch (Exception e) {
//            return null;
//        }
//        return null;
    }

    public static void saveString(String filename, String data) {
        try {
            saveRMS(filename, data.getBytes());
        } catch (Exception ex) {
        }
    }

    public static String loadString(String filename) {
        byte[] data = loadRMS(filename, 1);
        if (data == null) {
            return null;
        } else {
            return new String(data);
        }
    }

    public static void saveInt(String filename, int value) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);
        try {
            dos.writeInt(value);
            saveRMS(filename, baos.toByteArray());
        } catch (Exception ex) {
        } finally {
            try {
                dos.close();
            } catch (Exception ex) {
            }
        }

    }

    public static Integer loadInt(String filename) {
        byte[] data = loadRMS(filename, 1);
        if (data != null) {
            DataInputStream stream = new DataInputStream(new ByteArrayInputStream(data));
            int value;
            try {
                value = stream.readInt();
            } catch (Exception ex) {
                return null;
            } finally {
                try {
                    stream.close();
                } catch (Exception ex) {
                }
            }
            return new Integer(value);
        }
        return null;
    }
//
//    // set mot gia tri
//    public static void setInt(int value, int off, byte[] data) {
//        data[off] = (byte) ((value >> 24) & 0xff);
//        data[off + 1] = (byte) ((value >> 16) & 0xff);
//        data[off + 2] = (byte) ((value >> 8) & 0xff);
//        data[off + 3] = (byte) (value & 0xff);
//    }
//
//    //lay int tu byte
//    public static int getInt(int off, byte[] data) {
//        return (byte2int(data[off]) << 24) | (byte2int(data[off + 1]) << 16) | (byte2int(data[off + 2]) << 8) | byte2int(data[off + 3]);
//    }
//
//    //chuyen byte to int
//    public static int byte2int(byte b) {
//        return b & 0xff;
//    }

    public static void delete(String name) {
        try {
            RecordStore.deleteRecordStore(name);
        } catch (Exception ex) {
        }
    }

	//Ham ben mGo
	public static void clearAll() {
        String[] recNames = RecordStore.listRecordStores();
        if (recNames == null) {
            return;
        }
        for (int i = 0; i < recNames.length; i++) {
//            if (!recNames[i].equals("nick") && !recNames[i].equals("pass")) {
                try {
                    RecordStore.deleteRecordStore(recNames[i]);
                } catch (Exception ex) {
                }
//            }
        }
    }
	
    /**
     * Xóa dữ liệu
     * @param  type = 0: Clear All,
     *  type = 1 : Clear Background Images only.
     */
    public static void clearAll(int type) {
        String[] recNames = RecordStore.listRecordStores();
        if (recNames == null) {
            return;
        }
        for (int i = 0; i < recNames.length; i++) {
            if (!recNames[i].equals("nick") && !recNames[i].equals("pass") && !recNames[i].equals("lang")) {
                if (type == 1 && recNames[i].indexOf("bg") == -1) {
                    continue;
                }
                try {
                    RecordStore.deleteRecordStore(recNames[i]);
                } catch (Exception ex) {
                }
            }
        }
    }
//    public static final void setShort(short value, int off, byte[] data) {
//        data[off] = (byte) ((value >> 8) & 0xff);
//        data[off + 1] = (byte) (value & 0xff);
//    }
//
//    public static final short getShort(int off, byte[] data) {
//        return (short) ((byte2Short(data[off]) << 8) | byte2Short(data[off + 1]));
//    }
//
//    public static final short byte2Short(byte b) {
//        return (short) (b & 0xff);
//    }
}
