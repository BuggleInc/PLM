import sys

debug_fd = None

def get_answer_line():
    line = sys.stdin.readline()
    if line == "":
        sys.exit(1)

    if debug_fd:
        print(f"Answer: {line.rstrip()}", file=debug_fd)
        debug_fd.flush()

    return line.rstrip("\r\n")


def get_answer_int():
    return int(get_answer_line())


def get_answer_double():
    return float(get_answer_line())


def get_answer_string():
    return get_answer_line()


def get_answer_char():
    line = get_answer_line()
    return line[0] if line else ""


def send_command(format_string, *args):
    command = format_string % args

    if debug_fd:
        print(f"Command from C world: '{command}'", file=debug_fd)
        debug_fd.flush()

    print(command, file=sys.stderr)
    sys.stderr.flush()


# BEGIN UTILS FUNCTIONS

def int2str(nb):
    return str(nb)

# END UTILS FUNCTIONS