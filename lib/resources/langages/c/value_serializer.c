#include "value_serializer.h"
#include <assert.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define BUFF_SIZE 128

// --- Helper Utilities ---
typedef struct {
  char* data;
  size_t length;
  size_t capacity;
} string_builder_t;

static void sb_init(string_builder_t* sb)
{
  sb->capacity = 64;
  sb->length   = 0;
  sb->data     = malloc(sb->capacity);
  sb->data[0]  = '\0';
}

static void sb_append(string_builder_t* sb, const char* str)
{
  size_t len = strlen(str);
  while (sb->length + len + 1 > sb->capacity) {
    sb->capacity *= 2;
    sb->data = realloc(sb->data, sb->capacity);
  }
  strcpy(sb->data + sb->length, str);
  sb->length += len;
}

static void sb_free(string_builder_t* sb)
{
  free(sb->data);
}

// --- Format Parser Capacity Lookahead ---
static uint32_t count_elements_at_current_level(const char* p)
{
  uint32_t count = 0;
  int depth      = 0;
  while (*p) {
    if (*p == '[') {
      if (depth == 0)
        count++;
      depth++;
    } else if (*p == ']') {
      if (depth == 0)
        return count;
      depth--;
    } else {
      if (depth == 0)
        count++;
    }
    p++;
  }
  return count;
}

// --- Direct Serialization Logic ---
static void serialize_recursive(const plm_value_t* val, string_builder_t* sb)
{
  if (!val || val->type == PLM_VAL_NULL) {
    sb_append(sb, "Z");
    return;
  }

  char buffer[BUFF_SIZE + 1];
  switch (val->type) {
    case PLM_VAL_NULL:
      fprintf(stderr, "%s:%d: PLM_VAL_NULL was supported above, this line should not appear\n", __FILE__, __LINE__);
      exit(1);
    case PLM_VAL_INT:
      snprintf(buffer, BUFF_SIZE, "i%d", (int32_t)val->as.i);
      sb_append(sb, buffer);
      break;
    case PLM_VAL_DOUBLE:
      snprintf(buffer, BUFF_SIZE, "f%g", val->as.f);
      sb_append(sb, buffer);
      break;
    case PLM_VAL_BOOL:
      sb_append(sb, val->as.b ? "b1" : "b0");
      break;
    case PLM_VAL_CHAR:
      snprintf(buffer, BUFF_SIZE, "c%c", val->as.c);
      sb_append(sb, buffer);
      break;
    case PLM_VAL_COLOR:
      snprintf(buffer, BUFF_SIZE, "C%d", (int32_t)val->as.color);
      sb_append(sb, buffer);
      break;
    case PLM_VAL_STRING:
      sb_append(sb, "\"");
      for (const char* p = val->as.str; *p; p++) {
        if (*p == '\\' || *p == '"') {
          char esc[3] = {'\\', *p, '\0'};
          sb_append(sb, esc);
        } else {
          char c[2] = {*p, '\0'};
          sb_append(sb, c);
        }
      }
      sb_append(sb, "\"");
      break;
    case PLM_VAL_ARRAY: {
      bool all_same               = true;
      plm_value_type_t first_type = PLM_VAL_NULL;
      if (val->as.array.size > 0) {
        first_type = val->as.array.elements[0]->type;
        for (uint32_t i = 1; i < val->as.array.size; i++) {
          if (val->as.array.elements[i]->type != first_type) {
            all_same = false;
            break;
          }
        }
      }
      if (val->as.array.size > 0 && all_same && first_type != PLM_VAL_ARRAY && first_type != PLM_VAL_STRING && first_type != PLM_VAL_NULL) {
        if (first_type == PLM_VAL_INT)
          sb_append(sb, "i");
        else if (first_type == PLM_VAL_DOUBLE)
          sb_append(sb, "f");
        else if (first_type == PLM_VAL_BOOL)
          sb_append(sb, "b");
        else if (first_type == PLM_VAL_CHAR)
          sb_append(sb, "c");
        else if (first_type == PLM_VAL_COLOR)
          sb_append(sb, "C");
      }
      snprintf(buffer, BUFF_SIZE, "[%u", val->as.array.size);
      sb_append(sb, buffer);
      for (uint32_t i = 0; i < val->as.array.size; i++) {
        sb_append(sb, ":");
        serialize_recursive(val->as.array.elements[i], sb);
      }
      sb_append(sb, "]");
      break;
    }
  }
}

char* plm_serialize(const plm_value_t* val)
{
  string_builder_t sb;
  sb_init(&sb);
  serialize_recursive(val, &sb);
  return sb.data;
}

// --- Varargs Object Generation ---
plm_value_t* plm_vfrom_format(const char** fmt_ptr, va_list args)
{
  if (!**fmt_ptr || **fmt_ptr == '\0')
    return NULL;

  plm_value_t* val = calloc(1, sizeof(plm_value_t));
  char token       = **fmt_ptr;
  (*fmt_ptr)++;

  switch (token) {
    case 'Z':
      val->type = PLM_VAL_NULL;
      break;
    case 'i':
      val->type = PLM_VAL_INT;
      val->as.i = (uint32_t)va_arg(args, int);
      break;
    case 'f':
      val->type = PLM_VAL_DOUBLE;
      val->as.f = va_arg(args, double);
      break;
    case 'b':
      val->type = PLM_VAL_BOOL;
      val->as.b = (bool)va_arg(args, int);
      break;
    case 'c':
      val->type = PLM_VAL_CHAR;
      val->as.c = (char)va_arg(args, int);
      break;
    case 'C':
      val->type     = PLM_VAL_COLOR;
      val->as.color = (uint32_t)va_arg(args, uint32_t);
      break;
    case 's':
      val->type   = PLM_VAL_STRING;
      val->as.str = strdup(va_arg(args, const char*));
      break;
    case '[': {
      val->type          = PLM_VAL_ARRAY;
      val->as.array.size = count_elements_at_current_level(*fmt_ptr);
      if (val->as.array.size > 0) {
        val->as.array.elements = calloc(val->as.array.size, sizeof(plm_value_t*));
        for (uint32_t i = 0; i < val->as.array.size; i++) {
          val->as.array.elements[i] = plm_vfrom_format(fmt_ptr, args);
        }
      }
      if (**fmt_ptr == ']')
        (*fmt_ptr)++;
      break;
    }
    default:
      fprintf(stderr, "%s:%d: Unsupported format marker: %c\n", __FILE__, __LINE__, token);
      free(val);
      return NULL;
  }
  return val;
}

plm_value_t* plm_from_format(const char* fmt, ...)
{
  va_list args;
  va_start(args, fmt);
  const char* p    = fmt;
  plm_value_t* val = plm_vfrom_format(&p, args);
  va_end(args);
  return val;
}

char* plm_serialize_fmt(const char* fmt, ...)
{
  va_list args;
  va_start(args, fmt);
  const char* p    = fmt;
  plm_value_t* val = plm_vfrom_format(&p, args);
  va_end(args);
  char* res = plm_serialize(val);
  plm_value_free(val);
  return res;
}

// --- Deserialization Logic ---
static plm_value_t* parse_value(const char** p);

static plm_value_t* parse_array(const char** p)
{
  if (**p != '[')
    return NULL;
  (*p)++;
  plm_value_t* val       = calloc(1, sizeof(plm_value_t));
  val->type              = PLM_VAL_ARRAY;
  val->as.array.size     = strtoul(*p, (char**)p, 10);
  val->as.array.elements = calloc(val->as.array.size, sizeof(plm_value_t*));
  for (uint32_t i = 0; i < val->as.array.size; i++) {
    if (**p != ':')
      goto error;
    (*p)++;
    val->as.array.elements[i] = parse_value(p);
    if (!val->as.array.elements[i])
      goto error;
  }
  if (**p != ']')
    goto error;
  (*p)++;
  return val;
error:
  plm_value_free(val);
  return NULL;
}

static char* parse_string(const char** p)
{
  (*p)++;
  string_builder_t sb;
  sb_init(&sb);
  while (**p && **p != '"') {
    if (**p == '\\') {
      (*p)++;
      if (!**p) {
        sb_free(&sb);
        return NULL;
      }
    }
    char c[2] = {**p, '\0'};
    sb_append(&sb, c);
    (*p)++;
  }
  if (**p != '"') {
    sb_free(&sb);
    return NULL;
  }
  (*p)++;
  return sb.data;
}

static plm_value_t* parse_value(const char** p)
{
  plm_value_t* val = calloc(1, sizeof(plm_value_t));
  char c           = **p;
  if (c == 'Z') {
    val->type = PLM_VAL_NULL;
    (*p)++;
  } else if (c == 'i') {
    (*p)++;
    if (**p == '[') {
      free(val);
      return parse_array(p);
    }
    val->type = PLM_VAL_INT;
    val->as.i = (uint32_t)strtol(*p, (char**)p, 10);
  } else if (c == 'f') {
    (*p)++;
    if (**p == '[') {
      free(val);
      return parse_array(p);
    }
    val->type = PLM_VAL_DOUBLE;
    val->as.f = strtod(*p, (char**)p);
  } else if (c == 'b') {
    (*p)++;
    if (**p == '[') {
      free(val);
      return parse_array(p);
    }
    val->type = PLM_VAL_BOOL;
    val->as.b = (**p == '1');
    (*p)++;
  } else if (c == 'c') {
    (*p)++;
    if (**p == '[') {
      free(val);
      return parse_array(p);
    }
    val->type = PLM_VAL_CHAR;
    val->as.c = **p;
    (*p)++;
  } else if (c == 'C') {
    (*p)++;
    if (**p == '[') {
      free(val);
      return parse_array(p);
    }
    val->type     = PLM_VAL_COLOR;
    val->as.color = (uint32_t)strtol(*p, (char**)p, 10);
  } else if (c == '"') {
    val->type   = PLM_VAL_STRING;
    val->as.str = parse_string(p);
    if (!val->as.str) {
      free(val);
      return NULL;
    }
  } else if (c == '[') {
    free(val);
    return parse_array(p);
  } else {
    free(val);
    return NULL;
  }
  return val;
}

plm_value_t* plm_deserialize(const char* text)
{
  if (!text)
    return NULL;
  const char* p    = text;
  plm_value_t* val = parse_value(&p);
  if (val && *p != '\0') {
    plm_value_free(val);
    return NULL;
  }
  return val;
}

void plm_value_free(plm_value_t* val)
{
  if (!val)
    return;
  if (val->type == PLM_VAL_STRING)
    free(val->as.str);
  else if (val->type == PLM_VAL_ARRAY) {
    for (uint32_t i = 0; i < val->as.array.size; i++)
      plm_value_free(val->as.array.elements[i]);
    free(val->as.array.elements);
  }
  free(val);
}

// --- Native Conversions ---
uint32_t* plm_to_int_array(const plm_value_t* val)
{
  assert(val != NULL && val->type == PLM_VAL_ARRAY);
  uint32_t* arr = calloc(val->as.array.size, sizeof(uint32_t));
  for (uint32_t i = 0; i < val->as.array.size; i++) {
    assert(val->as.array.elements[i]->type == PLM_VAL_INT);
    arr[i] = val->as.array.elements[i]->as.i;
  }
  return arr;
}

uint32_t** plm_to_int_array_2d(const plm_value_t* val)
{
  assert(val != NULL && val->type == PLM_VAL_ARRAY);
  uint32_t** arr = calloc(val->as.array.size, sizeof(uint32_t*));
  for (uint32_t i = 0; i < val->as.array.size; i++) {
    arr[i] = plm_to_int_array(val->as.array.elements[i]);
  }
  return arr;
}

uint32_t*** plm_to_int_array_3d(const plm_value_t* val)
{
  assert(val != NULL && val->type == PLM_VAL_ARRAY);
  uint32_t*** arr = calloc(val->as.array.size, sizeof(uint32_t**));
  for (uint32_t i = 0; i < val->as.array.size; i++) {
    arr[i] = plm_to_int_array_2d(val->as.array.elements[i]);
  }
  return arr;
}

void plm_free_int_array_2d(uint32_t** arr, uint32_t rows)
{
  if (!arr)
    return;
  for (uint32_t i = 0; i < rows; i++)
    free(arr[i]);
  free(arr);
}

void plm_free_int_array_3d(uint32_t*** arr, uint32_t dim1, uint32_t dim2)
{
  if (!arr)
    return;
  for (uint32_t i = 0; i < dim1; i++)
    plm_free_int_array_2d(arr[i], dim2);
  free(arr);
}