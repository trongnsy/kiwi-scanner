package rn.sf.zxing;

import com.google.zxing.BarcodeFormat;

import java.util.Arrays;
import java.util.Collection;

public class StructureAppendScannerActivity extends SequenceScannerActivity {
    @Override
    protected Collection<BarcodeFormat> getBarcodeFormats() {
        return Arrays.asList(BarcodeFormat.QR_CODE);
    }
}
