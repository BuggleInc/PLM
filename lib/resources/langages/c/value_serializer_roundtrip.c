/* Reads one serialized value per line on stdin, and writes it back as serialized by the C glue (ERROR if it cannot parse it).
 * Used by ValueSerializerInteropTest. */
#include "value_serializer.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int main(void)
{
  char line[65536];
  while (fgets(line, sizeof(line), stdin)) {
    line[strcspn(line, "\n")] = '\0';
    plm_value_t* value        = plm_deserialize(line);
    if (value == NULL) {
      puts("ERROR");
      continue;
    }
    char* text = plm_serialize(value);
    puts(text);
    free(text);
    plm_value_free(value);
  }
  return 0;
}
