(library (chezcore package)
  (export
    pkg-prop
    pkg-name
    pkg-version 
    pkg-description 
    pkg-keywords 
    pkg-author 
    pkg-private
    pkg-scripts 
    pkg-dependencies 
    pkg-devDependencies
    )
  (import
    (scheme))

  (define get-package-file
    (lambda (name)
      (let ([dirs (filter 
                    file-exists?
                    (map 
                      (lambda (dir)
                        (format "~a~a~a~apackage.sc" (car dir) (directory-separator) name (directory-separator)))
                      (library-directories)))])
        (if (null? dirs) #f (car dirs)))))

  (define package-sc->scm
    (lambda (path)
      (call-with-input-file path read)))
      
  (define pkg-prop
    (lambda (lib prop-name)
      (let* ([path (get-package-file lib)]
              [pkg (and path (package-sc->scm path))]
              [prop (and pkg (assoc prop-name pkg))])
        (and prop (cdr prop))
      )
    ))

  (define pkg-name
    (lambda (name) (pkg-prop name "name")))

  (define pkg-version
    (lambda (name) (pkg-prop name "version")))

  (define pkg-description
    (lambda (name) (pkg-prop name "description")))

  (define pkg-keywords
    (lambda (name) (pkg-prop name "keywords")))

  (define pkg-author
    (lambda (name) (pkg-prop name "author")))

  (define pkg-private
    (lambda (name) (pkg-prop name "private")))

  (define pkg-scripts
    (lambda (name) (pkg-prop name "scripts")))

  (define pkg-dependencies
    (lambda (name) (pkg-prop name "dependencies")))

  (define pkg-devDependencies
    (lambda (name) (pkg-prop name "devDependencies")))
)


