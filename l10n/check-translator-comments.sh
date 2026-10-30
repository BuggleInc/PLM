#!/bin/sh
# Checks that xgettext extracts every "// Translator:" comment of the Java code: it only keeps the comments that are just before
# the line where their string starts, so a comment above a call that clang-format split is silently lost. Run by the i18n-update
# profile of Maven after the regeneration of the pot, on a fresh extraction (the pot keeps the comments of its previous entries).
# The keywords of xgettext are the ones of that profile.
mkdir -p target
find src -name '*.java' | LC_ALL=C sort |
  xgettext --output=target/translator-comments.pot --from-code=UTF-8 --language=Java -k -ktr -ktrc:1c,2 -ktrn:1,2 -ktrnc:1c,2,3 -kmarktr \
    --add-comments=Translator: --files-from=- || exit 1
missing=$(grep -rnoE '// Translator:.*' --include=*.java src | while IFS= read -r line; do
  grep -qF "#. ${line#*// }" target/translator-comments.pot || echo "$line"
done)
if [ -n "$missing" ]; then
  echo "xgettext lost these comments, which must be on the lines just before the one where their string starts:"
  echo "$missing"
  exit 1
fi
