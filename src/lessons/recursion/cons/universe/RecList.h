/* C counterpart of RecList.java: recursive sequence of integers.
 * Not part of the generated Remote*.h yet (C has no C_REMOTE_EXTRA_CODE
 * registration in CodeCreation.java), so entities include this header
 * directly instead of relying on an injected cons() primitive. */
#ifndef RECLIST_H
#define RECLIST_H

#include "../../../../../lib/resources/langages/c/value_serializer.h"
#include <stdlib.h>

typedef struct RecList {
  int head;
  struct RecList* tail;
} RecList;

static RecList* cons(int head, RecList* tail)
{
  RecList* r = malloc(sizeof(RecList));
  r->head    = head;
  r->tail    = tail;
  return r;
}

/* RecList.plmInsiderLength() */
static int recListLength(RecList* seq)
{
  int len = 0;
  for (RecList* ptr = seq; ptr != NULL; ptr = ptr->tail)
    len++;
  return len;
}

/* RecList.fromArray(int[]): builds a RecList from a deserialized int array param */
static RecList* recListFromValue(plm_value_t* val)
{
  RecList* res = NULL;
  for (int i = (int)val->as.array.size - 1; i >= 0; i--)
    res = cons((int)val->as.array.elements[i]->as.i, res);
  return res;
}

/* RecList.toArray(l) + serialize(int[]): turns a RecList back into a serializable value */
static plm_value_t* recListToValue(RecList* seq)
{
  plm_value_t* val       = calloc(1, sizeof(plm_value_t));
  val->type              = PLM_VAL_ARRAY;
  val->as.array.size     = recListLength(seq);
  val->as.array.elements = val->as.array.size ? malloc(val->as.array.size * sizeof(plm_value_t*)) : NULL;
  int i                  = 0;
  for (RecList* ptr = seq; ptr != NULL; ptr = ptr->tail) {
    plm_value_t* e              = calloc(1, sizeof(plm_value_t));
    e->type                     = PLM_VAL_INT;
    e->as.i                     = (uint32_t)ptr->head;
    val->as.array.elements[i++] = e;
  }
  return val;
}

#endif
