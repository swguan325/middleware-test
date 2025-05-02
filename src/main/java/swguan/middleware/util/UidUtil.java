
package swguan.middleware.util;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UidUtil {
    
    public static String getTimeUuid() {
        final SimpleDateFormat dformat = new SimpleDateFormat("yyyyMMddHHmmssSSS");
        final StringBuilder builder = new StringBuilder();
        builder.append(dformat.format(new Date()));
        builder.append("-");
        builder.append(UUID.randomUUID());
        return builder.toString();
    }
    
}

