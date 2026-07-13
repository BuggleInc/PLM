/*!
    Simple Unit Testing for C99, as a single header. Get the latest version at:
    https://framagit.org/mquinson/simple-unit-testing

    Copyright 2019-2020, Martin Quinson.
    Licence: GPL 3+

    Quick Start
    -----------

    - Drop the header in your source tree and include it in the files to be tested.
    - Declare at least one test case, using the `SUT_TEST` macro,
      and populate it with tests using the `SUT_*_EQUAL` or similar macros.
    - Declare at least one test suite using `SUT_TEST_SUITE`,
      and populate it with your test cases using `SUT_TEST_SUITE_ADD` and `SUT_TEST_SUITE_END`.
    - Create a runner for your tests: this C file should contain only `SUT_DECLARE_MAIN_FUNC`.

    If your code is split in several modules (each split in a header file and an implementation
    file) you probably want to add the test cases of this module directly in the implementation
    file, and declare a module-specific test suite (at the file bottom) to group all test cases.

    Declaring test cases
    --------------------

    The tests should be declared as follows, for example at the bottom of
    each source file if you want to test the internal implementation of
    your code, or in separate files if you only want to test the public
    interface.

       SUT_TEST(unique_name) {
         // Code of this test
       }
       SUT_TEST(another_cool_test_name) {
         // Code of this test
       }

    Writing tests' content
    ----------------------

    In each tests, you can use any of the following macro to test various things.
    You can first test whether two values are equal:

       SUT_CHAR_EQUAL(expected, actual, ...)
       SUT_INT_EQUAL(expected, actual, ...)
       SUT_DOUBLE_EQUAL(expected, actual, ...)
       SUT_STR_EQUAL(expected, actual, ...)

    It is sometimes annoying to find the difference between two strings,
    so SUT_STR_DIFF uses diff on disk to expose it more clearly.

       SUT_STR_DIFF(expected, actual, ...)

    You can also assert things.

       SUT_ASSERT(assertion, ...)
       SUT_ASSERT_TRUE(assertion, ...) // Exactly equivalent to SUT_ASSERT
       SUT_ASSERT_FALSE(assertion, ...) // fails if the assertion is true

    Any of these macro can be used with the minimal amount of parameters, eg:
       SUT_ASSERT_TRUE(my_func(toto) > 1)
    Or you can pass additional parameters that will be passed to fprintf(stderr), eg:
       SUT_ASSERT_TRUE(my_func(toto) > 1, "The value %d leads to a negative value %f", toto, my_func(toto))

    Grouping test cases into a test suite
    -------------------------------------

    Then, all tests are grouped within a suite as follows.
    You can have as many suites as you want in your project.

       SUT_TEST_SUITE(module_name) = {
           SUT_TEST_SUITE_ADD(unique_name),
           SUT_TEST_SUITE_ADD(another_cool_test_name),
           // possibly more tests added
           SUT_TEST_SUITE_END
       };

    That's all what you have to do to register your tests to the runner.

    Getting a runner
    ----------------

    You need to have a C file with the following content (only these 2 lines, unchanged):

       #include "simple_unit_test.h"

       SUT_DECLARE_MAIN_FUNC // Produces a main() function for your runner

    When executed, the resulting binary will run all tests one by one and report any errors encountered.
    You may want to run your test runner in valgrind for more insight.

    Troubleshooting
    ---------------

    * Don't forget the ending ';' after a SUT_TEST_SUITE declaration or you'll get weird errors in the code after it.

    If you have any other issue with this code, please open an issue on FramaGit.

    TODO
    ----
    - Write some examples and maybe more doc
    - The code supports setup and teardown functions, but not the public API
    - The runner is minimalistic. Without going for the full fork() game, we could give the suite names
 */

#ifndef __SIMPLE_UNIT_TESTING_H__
#define __SIMPLE_UNIT_TESTING_H__

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>

typedef int (*sut_test_func_t)(void);

typedef struct s_sut_test_t {
  sut_test_func_t func_;
  sut_test_func_t setup_;
  sut_test_func_t teardown_;
} sut_test_t;

typedef struct s_sut_testsuite_t {
  const char* name_;
  sut_test_t* tests_;
  int size_;
} sut_testsuite_t;

typedef struct s_sut_registery_t {
  sut_testsuite_t* suites_;
  int capacity_;
  int size_;
} sut_registery_t;

/*
 * Expands to `one' if there is only one argument for the variadic part.
 * Otherwise, expands to `more'.
 * Works with up to 63 arguments, which is the maximum mandated by the C99 standard.
 */
#define _SUT_IF_ONE_ARG(one, more, ...)                                                                                                                        \
  _SUT_IF_ONE_ARG_(__VA_ARGS__, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more,  \
                   more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more,   \
                   more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, more, one)
#define _SUT_IF_ONE_ARG_(a64, a63, a62, a61, a60, a59, a58, a57, a56, a55, a54, a53, a52, a51, a50, a49, a48, a47, a46, a45, a44, a43, a42, a41, a40, a39,     \
                         a38, a37, a36, a35, a34, a33, a32, a31, a30, a29, a28, a27, a26, a25, a24, a23, a22, a21, a20, a19, a18, a17, a16, a15, a14, a13,     \
                         a12, a11, a10, a9, a8, a7, a6, a5, a4, a3, a2, a1, N, ...)                                                                            \
  N

#define SUT_EQUAL(expected, actual, fmt, ...)                                                                                                                  \
  if ((expected) != (actual)) {                                                                                                                                \
    fprintf(stderr,                                                                                                                                            \
            "\x1b[1m\x1b[31mERROR in %s:%d:%s: \x1b[0m"                                                                                                        \
            " expected %s == %s, but " fmt " != " fmt ".",                                                                                                     \
            __FILE__, __LINE__, __FUNCTION__, #expected, #actual, expected, actual);                                                                           \
    _SUT_IF_ONE_ARG(fprintf(stderr, "\n"), fprintf(stderr, __VA_ARGS__)(_VA_ARGS__));                                                                          \
    return 0;                                                                                                                                                  \
  }

#define SUT_CHAR_EQUAL(expected, actual, ...) SUT_EQUAL(expected, actual, "'%c'", __VA_ARGS__)
#define SUT_INT_EQUAL(expected, actual, ...) SUT_EQUAL(expected, actual, "%d", __VA_ARGS__)
#define SUT_DOUBLE_EQUAL(expected, actual, ...) SUT_EQUAL(expected, actual, "%f", __VA_ARGS__)

#define SUT_STR_EQUAL(expected, actual, ...)                                                                                                                   \
  if (strcmp((expected), (actual))) {                                                                                                                          \
    fprintf(stderr,                                                                                                                                            \
            "\x1b[1m\x1b[31mERROR in %s:%d:%s: \x1b[0m"                                                                                                        \
            " expected %s == %s, but '%s' != '%s'.",                                                                                                           \
            __FILE__, __LINE__, __FUNCTION__, #expected, #actual, expected, actual);                                                                           \
    _SUT_IF_ONE_ARG(fprintf(stderr, "\n"), fprintf(stderr, __VA_ARGS__)(__VA_ARGS__));                                                                         \
    return 0;                                                                                                                                                  \
  }
#define SUT_STR_DIFF(expected, actual, ...)                                                                                                                    \
  if (strcmp((expected), (actual))) {                                                                                                                          \
    fprintf(stderr,                                                                                                                                            \
            "\x1b[1m\x1b[31mERROR in %s:%d:%s: \x1b[0m"                                                                                                        \
            " expected %s == %s, but '%s' != '%s'.",                                                                                                           \
            __FILE__, __LINE__, __FUNCTION__, #expected, #actual, expected, actual);                                                                           \
    FILE* _fh = fopen("/tmp/simpleunittest_expected", "w");                                                                                                    \
    fprintf(_fh, "%s", expected);                                                                                                                              \
    fclose(_fh);                                                                                                                                               \
    _fh = fopen("/tmp/simpleunittest_actual", "w");                                                                                                            \
    fprintf(_fh, "%s", actual);                                                                                                                                \
    fclose(_fh);                                                                                                                                               \
    system("diff -u /tmp/simpleunittest_expected /tmp/simpleunittest_actual");                                                                                 \
    unlink("/tmp/simpleunittest_expected");                                                                                                                    \
    unlink("/tmp/simpleunittest_actual");                                                                                                                      \
    _SUT_IF_ONE_ARG(fprintf(stderr, "\n"), fprintf(stderr, __VA_ARGS__)(__VA_ARGS__));                                                                         \
    return 0;                                                                                                                                                  \
  }

#define SUT_ASSERT(assertion, ...)                                                                                                                             \
  if (!(assertion)) {                                                                                                                                          \
    fprintf(stderr,                                                                                                                                            \
            "\x1b[1m\x1b[31mERROR in %s:%d:%s: \x1b[0m"                                                                                                        \
            " assertion failed: \"%s\"\n",                                                                                                                     \
            __FILE__, __LINE__, __FUNCTION__, #assertion);                                                                                                     \
    _SUT_IF_ONE_ARG(fprintf(stderr, "\n"), fprintf(stderr, __VA_ARGS__)(__VA_ARGS__));                                                                         \
    return 0;                                                                                                                                                  \
  }
#define SUT_ASSERT_FALSE(assertion, ...) SUT_ASSERT(!assertion, __VA_ARGS__)
#define SUT_ASSERT_TRUE(assertion, ...) SUT_ASSERT(assertion, __VA_ARGS__)

#define SUT_TEST(name) static int sut_test_case_##name()

#pragma weak sut_global_registery
#define SUT_TEST_SUITE(suiteName)                                                                                                                              \
  extern sut_test_t test_suite_##suiteName[];   /* Declared below */                                                                                           \
  extern sut_registery_t* sut_global_registery; /* Declared weak */                                                                                            \
  __attribute__((constructor)) static void sut_register_##suiteName()                                                                                          \
  {                                                                                                                                                            \
    if (sut_global_registery == NULL) {                                                                                                                        \
      sut_global_registery            = malloc(sizeof(sut_registery_t));                                                                                       \
      sut_global_registery->suites_   = malloc(sizeof(sut_testsuite_t) * 10);                                                                                  \
      sut_global_registery->capacity_ = 10;                                                                                                                    \
      sut_global_registery->size_     = 0;                                                                                                                     \
    }                                                                                                                                                          \
    sut_registery_t* reg = sut_global_registery;                                                                                                               \
    if (reg->capacity_ == reg->size_) {                                                                                                                        \
      reg->capacity_ += 10;                                                                                                                                    \
      reg->suites_ = realloc(reg->suites_, reg->capacity_ * sizeof(sut_testsuite_t));                                                                          \
    }                                                                                                                                                          \
    sut_testsuite_t* suite = &reg->suites_[reg->size_++];                                                                                                      \
    suite->name_           = #suiteName;                                                                                                                       \
    /* Compute the suite size by iterating over it (it's NULL-terminated) */                                                                                   \
    suite->size_  = 0;                                                                                                                                         \
    sut_test_t* t = test_suite_##suiteName;                                                                                                                    \
    while (t->func_ != NULL) {                                                                                                                                 \
      t++;                                                                                                                                                     \
      suite->size_++;                                                                                                                                          \
    }                                                                                                                                                          \
    /* Malloc enough size to copy the tests in position */                                                                                                     \
    suite->tests_ = malloc(suite->size_ * sizeof(sut_test_t));                                                                                                 \
    memcpy(suite->tests_, &test_suite_##suiteName, suite->size_ * sizeof(sut_test_t));                                                                         \
  }                                                                                                                                                            \
  sut_test_t test_suite_##suiteName[]
#define SUT_TEST_SUITE_ADD(func) {sut_test_case_##func, NULL, NULL}
#define SUT_TEST_SUITE_END {NULL, NULL, NULL}

#define SUT_DECLARE_MAIN_FUNC                                                                                                                                  \
  sut_registery_t* sut_global_registery = NULL; /* Declared weak */                                                                                            \
  int main(/*int argc, char* argv[]*/)                                                                                                                         \
  {                                                                                                                                                            \
    sut_registery_t* registry = sut_global_registery;                                                                                                          \
    int okTests               = 0;                                                                                                                             \
    int failedTests           = 0;                                                                                                                             \
    for (int suiterank = 0; suiterank < registry->size_; suiterank++) {                                                                                        \
      sut_testsuite_t* suite = &registry->suites_[suiterank];                                                                                                  \
      for (int testrank = 0; testrank < suite->size_; testrank++) {                                                                                            \
        sut_test_t* test = &suite->tests_[testrank];                                                                                                           \
        if (test->setup_) {                                                                                                                                    \
          (*test->setup_)();                                                                                                                                   \
        }                                                                                                                                                      \
        int retcode = (*test->func_)();                                                                                                                        \
        if (test->teardown_) {                                                                                                                                 \
          (*test->teardown_)();                                                                                                                                \
        }                                                                                                                                                      \
        if (retcode) {                                                                                                                                         \
          printf(".");                                                                                                                                         \
          ++okTests;                                                                                                                                           \
        } else {                                                                                                                                               \
          printf("x");                                                                                                                                         \
          ++failedTests;                                                                                                                                       \
        }                                                                                                                                                      \
        fflush(stdout);                                                                                                                                        \
      }                                                                                                                                                        \
      free(suite->tests_);                                                                                                                                     \
    }                                                                                                                                                          \
    printf("\nOK: %d tests passed.", okTests);                                                                                                                 \
    if (failedTests != 0)                                                                                                                                      \
      printf("\x1b[1m\x1b[31m%d tests FAILED.\x1b[0m", failedTests);                                                                                           \
    printf("\n");                                                                                                                                              \
    free(registry->suites_);                                                                                                                                   \
    free(registry);                                                                                                                                            \
    return failedTests ? 1 : 0;                                                                                                                                \
  }

#endif
