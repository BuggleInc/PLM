#ifndef VALUE_SERIALIZER_H
#define VALUE_SERIALIZER_H

#include <stdarg.h>
#include <stdbool.h>
#include <stdint.h>

typedef enum { PLM_VAL_NULL, PLM_VAL_INT, PLM_VAL_DOUBLE, PLM_VAL_BOOL, PLM_VAL_CHAR, PLM_VAL_COLOR, PLM_VAL_STRING, PLM_VAL_ARRAY } plm_value_type_t;

typedef struct plm_value_t plm_value_t;

struct plm_value_t {
  plm_value_type_t type;
  union {
    uint32_t i;
    double f;
    bool b;
    char c;
    uint32_t color;
    char* str;
    struct {
      plm_value_t** elements;
      uint32_t size;
    } array;
  } as;
};

// Core API
plm_value_t* plm_deserialize(const char* text);
char* plm_serialize(const plm_value_t* val);
void plm_value_free(plm_value_t* val);

// Format-Driven Builders (Variadic Solutions)
plm_value_t* plm_from_format(const char* fmt, ...);
plm_value_t* plm_vfrom_format(const char** fmt_ptr, va_list args);
char* plm_serialize_fmt(const char* fmt, ...);

// Native Array Conversion Helpers (abort on type mismatch)
uint32_t* plm_to_int_array(const plm_value_t* val);
uint32_t** plm_to_int_array_2d(const plm_value_t* val);
uint32_t*** plm_to_int_array_3d(const plm_value_t* val);

void plm_free_int_array_2d(uint32_t** arr, uint32_t rows);
void plm_free_int_array_3d(uint32_t*** arr, uint32_t dim1, uint32_t dim2);

#endif // VALUE_SERIALIZER_H