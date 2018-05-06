(library (chezcore ffi)
  (export
    define-procedure
    make-ftype-null 
    char*   
    char*->string
    char**->vector
    )
  (import (scheme))

  (define-syntax define-procedure
    (syntax-rules ()
      ((_ name sym args rst)
        (define name
          (if (foreign-entry? sym)
            (foreign-procedure sym args rst)
            (lambda x (printf "ffi-error: ~a not found\n" sym)))))))

  (define-syntax make-ftype-null
    (syntax-rules ()
      ((_ type)
        (make-ftype-pointer type #x00000000))))

  ;; address:  the address value of a char pointer
  ;; eg: (define a (foreign-procedure "fun1" () (* char)))
  ;;       => (char*->string (ftype-pointer-address (a)))
  ;; or  (define a (foreign-procedure "fun1" () void*))
  ;;       => (char*->string (a))
  (define char*->string
    (lambda (address)
      (list->string
        (reverse
          (let loop ([address address] [chars '()])
            (let ([char (foreign-ref 'char address 0)])
              (if (char=? char #\nul)
                  chars
                  (loop (+ 1 address) (cons char chars)))))))))
  
  (define-ftype char*  (* char))

  ;; pointer: a pointer of char*
  ;; length: array length
  ;; c: char** fun1();
  ;; scm: 1. (define fun1 (foreign-procedure "fun1" () (* char*)))
  ;;      2. (char**->vector (fun1) len)
  (define char**->vector
    (lambda (pointer length)
      (let* ([vec (make-vector length)])
        (let loop ([idx 0])
          (if (= idx length)
            vec
            (begin
              (vector-set! vec idx (parse-cell pointer idx))
              (loop (+ idx 1))
            ))))))

  (define parse-cell
    (lambda (pointer index)
      (if (null-cell? pointer index)
        'null
        (cell-string pointer index))))

  (define null-cell?
    (lambda (pointer index)
      (eq? 0 (foreign-ref 'unsigned-8
                          (ftype-pointer-address
                            (ftype-&ref char* () pointer index))
                          0))))

  (define cell-string
    (lambda (pointer index)
      (char*->string
        (ftype-pointer-address
          (ftype-&ref char* (*) pointer index)))))
)