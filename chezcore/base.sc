(library (chezcore base)
  (export
    class-of
    ->string
    try)
  (import (scheme))
  
  (define class-of
    (lambda (x)
      (cond
        ((string=? x) "string")
        ((symbol? x) "symbol")
        ((number? x) "number")
        ((procedure? x) "procedure")
        ((null? x) "null")
        ((list? x) "list")
        ((pair? x) "pair")
        ((boolean? x) "boolean")
        ((char? x) "char")
        ((vector? x) "vector")
        ((bytevector? x) "bytevector")
        ((hashtable? x) "hashtable")
        ((record? x) "record")
        ((ftype-pointer? x) "ftype-pointer")
        ((date? x) "date")
        ((time? x) "time")
        (else (format "unknown class of ~a\n"))
      )
    )
  )

  (define (->string x)
    (with-output-to-string (lambda () (display x))))
    
  (define-syntax try 
    (syntax-rules (catch) 
      ((_ body (catch catcher)) 
        (call/cc 
          (lambda (exit) 
            (with-exception-handler 
              (lambda (condition) 
                (catcher condition) 
                (exit condition)) 
              (lambda () body)))))))
  )