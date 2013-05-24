#!/usr/bin/env python
## Initial source code from https://github.com/denmark/html2markdown/

from HTMLParser import HTMLParser

import re
import os
import sys
import string



class Html2MarkdownParser(HTMLParser):
    def __init__(self):
        self.indice_code=[]
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

    def startSelectLanguage(self):
        code=self._tag_attr_data.get('class')
        if code=='Java':
            self._append_to_markdown('\n<java>')
            self.indice_code.append(1)
        elif code=='Python':
            self._append_to_markdown('\n<python>')
            self.indice_code.append(2)
        elif code=='comment':
            self._append_to_markdown('\n<comment>')
            self.indice_code.append(3)
        else:
            self.indice_code.append(0)

    def endSelectLanguage(self):
        c=self.indice_code.pop()
        if c==1:
            self._append_to_markdown('</java>')
        elif c==2:
            self._append_to_markdown('</python>')
        elif c==3:
            self._append_to_markdown('</comment>')

    # <a />
    def handle_start_a(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()

    def handle_end_a(self):
        a_tag = ''
        a_tag += '[' + self._handled_tag_body_data + ']'
        a_tag += '('
        if self._tag_attr_data.get('href'):
            a_tag +=  self._tag_attr_data.get('href')

        title = self._tag_attr_data.get('title')
        if title:
            a_tag += ' "' + title + '") '
        else:
            a_tag += ') '
        self._append_to_markdown(a_tag)
        self.endSelectLanguage()


    # <table />
    def handle_start_table(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('\n\n<table')
        a_tag=''
        if self._tag_attr_data.get('border'):
            a_tag += 'border=' + self._tag_attr_data.get('border')
        self._append_to_markdown(a_tag+'>\n')
    def handle_end_table(self):
        a_tag = self._handled_tag_body_data
        a_tag += '</table>\n\n'
        self._append_to_markdown(a_tag)
        self.endSelectLanguage()

    # <tr />
    def handle_start_tr(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('\t<tr>\n')
    def handle_end_tr(self):
        #self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown(self._handled_tag_body_data + '\t</tr>\n')
        self.endSelectLanguage()

    # <td />
    def handle_start_td(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('\t\t<td')
        a_tag=''
        if self._tag_attr_data.get('align'):
            a_tag = 'align="' + self._tag_attr_data.get('align')+'"'
        self._append_to_markdown(a_tag+'>')
    def handle_end_td(self):
        #self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown(self._handled_tag_body_data + '</td>\n')
        self.endSelectLanguage()

    # <font />
    def handle_start_font(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('<font')
        a_tag=''
        if self._tag_attr_data.get('size'):
            a_tag = 'size="' + self._tag_attr_data.get('size')+'"'
        self._append_to_markdown(a_tag+'>')
    def handle_end_font(self):
        #self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown(self._handled_tag_body_data + '</font>')
        self.endSelectLanguage()

    # <img />
    def handle_start_img(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()

    def handle_end_img(self):
        a_tag = ''
        a_tag += '<img' + self._handled_tag_body_data
        a_tag += ' src="' + self._tag_attr_data.get('src')+'"'

        title = self._tag_attr_data.get('title')
        if title:
            a_tag += ' title="' + title + '"'
        a_tag += ' />'
        self._append_to_markdown(a_tag)
        self.endSelectLanguage()

    # <code />
    def handle_start_code(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('`')
    def handle_end_code(self):
        #self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown(self._handled_tag_body_data + '`')
        self.endSelectLanguage()

    # <span />
    def handle_start_span(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('(')
    def handle_end_span(self):
        #self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown(self._handled_tag_body_data + ')')
        self.endSelectLanguage()


    # <div />
    def handle_start_div(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('  \n  \n')
    def handle_end_div(self):
        #self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown(self._handled_tag_body_data + '  \n  \n')
        self.endSelectLanguage()


    # <tt />
    def handle_start_tt(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
    def handle_end_tt(self):
        #self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown(self._handled_tag_body_data)
        self.endSelectLanguage()


    # <b />
    def handle_start_b(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
    def handle_end_b(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('**' + self._handled_tag_body_data + '**')
        self.endSelectLanguage()

    # <blockquote />
    def handle_start_blockquote(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
    def handle_end_blockquote(self):
        blockquote_body = self._handled_tag_body_data.split(os.linesep)

        for blockquote_line in blockquote_body:
            blockquote_line = blockquote_line.strip()
            self._append_to_markdown('> ' + blockquote_line + os.linesep)
        self.endSelectLanguage()

    # <em />
    def handle_start_em(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
    def handle_end_em(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('*' + self._handled_tag_body_data + '*')
        self.endSelectLanguage()

    # <i />
    def handle_start_i(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
    def handle_end_i(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('*' + self._handled_tag_body_data + '*')
        self.endSelectLanguage()

    # <h1 />
    def handle_start_h1(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('\n# ')
    def handle_end_h1(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown(self._handled_tag_body_data + ' #' + os.linesep)
        self.endSelectLanguage()

    # <h2 />
    def handle_start_h2(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('\n## ')
    def handle_end_h2(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown( self._handled_tag_body_data + ' ##' + os.linesep)
        self.endSelectLanguage()

    # <h3 />
    def handle_start_h3(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('\n### ')
    def handle_end_h3(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown(self._handled_tag_body_data + ' ###' + os.linesep)
        self.endSelectLanguage()

    # <h4 />
    def handle_start_h4(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('\n#### ')
    def handle_end_h4(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown( self._handled_tag_body_data + ' ####' + os.linesep)
        self.endSelectLanguage()

    # <h5 />
    def handle_start_h5(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('\n##### ')
    def handle_end_h5(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown(self._handled_tag_body_data + ' #####' + os.linesep)
        self.endSelectLanguage()

    # <h6 />
    def handle_start_h6(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('\n###### ')
    def handle_end_h6(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown(self._handled_tag_body_data + ' ######' + os.linesep)
        self.endSelectLanguage()

    # <hr />
    def handle_start_hr(self, attrs):
        self._append_to_markdown('* * *' + os.linesep)

    # <li />
    def handle_start_ol(self, attrs):
        self._append_to_markdown('  \n  \n')
    def handle_start_ul(self, attrs):
        self._append_to_markdown('  \n  \n')
    def handle_start_li(self, attrs):
        if len(self._tag_stack):
            if self._tag_stack[0] == 'ol':
                self._append_to_markdown('1.  ')
            elif self._tag_stack[0] == 'ul':
                self._append_to_markdown('*  ')
    def handle_end_li(self):
        if len(self._tag_stack):
            if self._tag_stack[-1] == 'ol':
                self._append_to_markdown(self._handled_tag_body_data + '  ' + os.linesep)
            elif self._tag_stack[-1] == 'ul':
                self._append_to_markdown(self._handled_tag_body_data + '  ' + os.linesep)

    # <p />
    def handle_start_p(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        if len(self._markdown) > 1:
            if self._markdown[-2:] == '%s%s' % (os.linesep, os.linesep):
                pass
            elif self._markdown[-1:] == os.linesep:
                self._markdown += os.linesep
            else:
                self._markdown += os.linesep + os.linesep

    def handle_end_p(self):
        self._markdown += '%s%s' % (os.linesep, os.linesep)
        self.endSelectLanguage()

    # <pre />
    def handle_start_pre(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
        self._append_to_markdown('\n<pre>')
    def handle_end_pre(self):
        a_tag = self._handled_tag_body_data
        a_tag += '</pre>\n'
        self._append_to_markdown(a_tag)
        self.endSelectLanguage()

    # <strong />
    def handle_start_strong(self, attrs):
        self._tag_attr_data = dict(attrs)
        self.startSelectLanguage()
    def handle_end_strong(self):
        self._handled_tag_body_data = self._handled_tag_body_data.replace(os.linesep, ' ')
        self._append_to_markdown('**' + self._handled_tag_body_data + '**')
        self.endSelectLanguage()

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
        if len(self._tag_stack) and self._tag_stack[-1] not in ['p','tr','td']:
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

