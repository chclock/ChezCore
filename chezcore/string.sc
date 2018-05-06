(library (chezcore string)
  (export
    string-trim-start
    string-trim-end
    string-trim
    string-split)
  (import (scheme))

  (define string-trim-start
    (lambda (str)
      (let loop ([idx 0]
                 [len (string-length str)])
        (if (= idx len)
          ""
          (if (char=? #\space (string-ref str idx))
            (loop (+ 1 idx) len)
            (substring str idx len)
          )))))
  
  (define string-trim-end
    (lambda (str)
      (let loop ([idx (- (string-length str) 1)])
        (if (= idx -1)
          ""
          (if (char=? #\space (string-ref str idx))
            (loop (- idx 1))
            (substring str 0 (+ 1 idx))
          )))))

  (define string-trim
    (lambda (str)
      (string-trim-end (string-trim-start str))))
  
  (define string-split
    (lambda (s c)
      (letrec* ([len (string-length s)]
                [walk (lambda (str begin end rst)
                        (cond ((>= begin len) rst)
                              ((or (= end len) (char=? (string-ref str end) c))
                                (walk
                                  str 
                                  (+ end 1)
                                  (+ end 1)
                                  (if (= begin end) 
                                    rst
                                    (cons (substring str begin end) rst))))
                              (else
                                (walk str begin (+ end 1) rst))))])
        (reverse (walk s 0 0 '())))))
)