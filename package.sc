(("name" . "chezcore")
("version" . "0.1.0")
("description" . "chez core extension")
("keywords")
("author" 
    ("ch"))
("private" . #f)
("scripts" 
    ("run" . "scheme --script")
    ("repl" . "scheme")
    ("dev" . "gcc -fPIC -shared -o  hello.so hello.c")
    ("test" . "raven run test.ss"))
("dependencies")
("devDependencies"))