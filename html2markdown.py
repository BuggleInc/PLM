#!/usr/bin/env python

from HTMLParser import HTMLParser

import re
import os
import sys
import string

class Html2MarkdownParser(HTMLParser):
    def __init__(self):
        self._markdown = ''
        self._tag_stack = []
        self._tag_attr_data = {}
        self._handled_tag_body_data = ''
        self._convertible_tags = ['a',
                                  'b', 'blockquote',
                                  'em',
                                  'h1', 'h2', 'h3', 'h4', 'h5', 'h6', 'hr',
                                  'ol',
                                  'p', 'pre',
                                  'strong',
                                  'ul']
        # FIXME: special characters
        HTMLParser.__init__(self)

    def _append_to_markdown(self, new_markdown):
        if len(self._markdown) > 1:
            if re.match('\s', self._markdown[-1:]):
                self._markdown += new_markdown
            else:
                self._markdown += ' ' + new_markdown
        else:
            self._markdown += new_markdown

    # <a />
    def handle_start_a(self, attrs):
        self._tag_attr_data = dict(attrs)

    def handle_end_a(self):
        a_tag = ''
        a_tag += '[' + self._handled_tag_body_data + ']'
        a_tag += '(' + self._tag_attr_data.get('href')

        title = self._tag_attr_data.get('title')
        if title:
            a_tag += ' "' + title + '") '
        else:
            a_tag += ') '
        self._append_to_markdown(a_tag)

    # <b />
    def handle_end_b(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('*' + self._handled_tag_body_data + '*')

    # <blockquote />
    def handle_end_blockquote(self):
        blockquote_body = self._handled_tag_body_data.split(os.linesep)

        for blockquote_line in blockquote_body:
            blockquote_line = blockquote_line.strip()
            self._append_to_markdown('> ' + blockquote_line + os.linesep)

    # <em />
    def handle_end_em(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('*' + self._handled_tag_body_data + '*')

    # <h1 />
    def handle_end_h1(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('# ' + self._handled_tag_body_data + ' #' + os.linesep)

    # <h2 />
    def handle_end_h2(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('## ' + self._handled_tag_body_data + ' ##' + os.linesep)

    # <h3 />
    def handle_end_h3(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('### ' + self._handled_tag_body_data + ' ###' + os.linesep)

    # <h4 />
    def handle_end_h4(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('#### ' + self._handled_tag_body_data + ' ####' + os.linesep)

    # <h5 />
    def handle_end_h5(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('##### ' + self._handled_tag_body_data + ' #####' + os.linesep)

    # <h6 />
    def handle_end_h6(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('###### ' + self._handled_tag_body_data + ' ######' + os.linesep)

    # <hr />
    def handle_start_hr(self, attrs):
        self._append_to_markdown('* * *' + os.linesep)

    # <li />
    def handle_end_li(self):
        if len(self._tag_stack):
            if self._tag_stack[-1] == 'ol':
                self._append_to_markdown('1.    ' + self._handled_tag_body_data + os.linesep)
            elif self._tag_stack[-1] == 'ul':
                self._append_to_markdown('*    ' + self._handled_tag_body_data + os.linesep)

    # <p />
    def handle_start_p(self, attrs):
        if len(self._markdown) > 1:
            if self._markdown[-2:] == '%s%s' % (os.linesep, os.linesep):
                pass
            elif self._markdown[-1:] == os.linesep:
                self._markdown += os.linesep
            else:
                self._markdown += os.linesep + os.linesep

    def handle_end_p(self):
        self._markdown += '%s%s' % (os.linesep, os.linesep)

    # <pre />
    def handle_end_pre(self):
        code_lines = self._handled_tag_body_data.split(os.linesep)
        for code_line in code_lines:
            code_line = code_line.strip()
            self._append_to_markdown('    ' + code_line + os.linesep)

    # <strong />
    def handle_end_strong(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('**' + self._handled_tag_body_data + '**')

    ## ###
    def handle_starttag(self, tag, attrs):
        self._tag_stack.append(tag)
        try:
            eval('self.handle_start_' + tag + '(attrs)')
        except AttributeError, e:
            pass

    def handle_endtag(self, tag):
        self._tag_stack.pop()
        try:
            eval('self.handle_end_' + tag + '()')
            # Collapse three successive CRs into two before moving on
            while len(self._markdown) > 2 and \
                    self._markdown[-3:] == '%s%s%s' % (os.linesep, os.linesep, os.linesep):
                self._markdown = self._markdown[:-3] + '%s%s' % (os.linesep, os.linesep)
        except AttributeError, e:
            pass

        self._tag_attr_data = {}
        self._handled_tag_body_data = ''

    def handle_data(self, data):
        data = os.linesep.join(map(string.strip, data.strip().split(os.linesep)))
        if len(self._tag_stack) and self._tag_stack[-1] not in ['p']:
            self._handled_tag_body_data += data
        else:
            self._append_to_markdown(data)

    def get_markdown(self):
        return self._markdown.rstrip() + '\n'

def main():
    p = Html2MarkdownParser()
    buf = sys.stdin.read()
    p.feed(buf)
    p.close()
    print p.get_markdown()

if __name__ == "__main__":
    sys.exit(main())