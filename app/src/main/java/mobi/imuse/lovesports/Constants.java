/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package mobi.imuse.lovesports;

import android.os.Environment;
import android.provider.MediaStore.Images;

public final class Constants {
    public static final boolean DEVELOPER_MODE = false;
    public static final String BASE_CONTENT_STRING_IMAGES = (Images.Media.EXTERNAL_CONTENT_URI).toString() + "/";
    public static final String BasePhotoUrlDiskCached = Environment.getExternalStorageDirectory()+"/LoveSports/Caches";

    // Base Url;
    public static final String BASE_URL = "http://1.2.3.4/";

    public static final String QINIU_ACCESS_KEY = "k74kwexXovTZpEBtbeys25ID3CxLtW2oWPhsNnOL";
    public static final String QINIU_SECRET_KEY = "dxaLtIKj8ObKaA0b45L0ZVAP53RjFanM2YeKeoTb";
    public static final String QINIU_BUCKET_NAME = "sports";

}
