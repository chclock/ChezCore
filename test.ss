 
(import (chezcore chezmatch))
(load-match)
(display (match '(a 17 37)
  [(a ,x) 1]
  [(b ,x ,y) 2]
  [(a ,x ,y) 3]))