(library (chezcore library)
  (export
    include/resolve)
  (import (scheme))

  ;; surface private include
  ;; 加载文件到library中
  (define-syntax include/resolve
    (lambda (stx)
      (define (include/lexical-context ctxt filename)
        (with-exception-handler
          (lambda (ex)
            (raise
              (condition
                (make-error)
                (make-who-condition 'include/resolve)
                (make-message-condition "error while trying to include")
                (make-irritants-condition (list filename))
                (if (condition? ex) ex (make-irritants-condition (list ex))))))
          (lambda ()
            (call-with-input-file filename
              (lambda (fip)
                (let loop ([a '()])
                  (let ([x (read fip)])
                    (if (eof-object? x)
                        (cons #'begin (datum->syntax ctxt (reverse a)))
                        (loop (cons x a))))))))))
      (syntax-case stx ()
        [(ctxt (lib-path* ...) file-path)
          (for-all (lambda (s) 
                      (and (string? s) (positive? (string-length s)))) 
                   (syntax->datum #'(lib-path* ... file-path)))
            (let ([p (apply string-append 
                        (map (lambda (ps) (string-append "/" ps)) 
                                (syntax->datum #'(lib-path* ... file-path))))]
                  [sp (map car (library-directories))])
            (let loop ([search sp])
                (if (null? search)
                  (error 'include/resolve "cannot find file in search paths"
                    (substring p 1 (string-length p)) sp)
                  (let ([full (string-append (car search) p)])
                    (if (file-exists? full)
                      (include/lexical-context #'ctxt full)
                      (loop (cdr search)))))))])))
)