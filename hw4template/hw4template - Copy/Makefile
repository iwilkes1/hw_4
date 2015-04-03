###############
# LaTeX makefile by Ben Mitchell (ben@cs.jhu.edu), released under CC-BY license
# Last updated: 2015-01-09
#
# Just do `make <myfile>.pdf` to generate <myfile>.pdf from <myfile>.tex
# (this should work with any new .tex file you create in this directory).
# Alternatively, just do `make` to generate hw4template.pdf.
###############

##### Defined variables #####

# whether to use pdflatex (true) or old-style (DVI) latex (false)
PDF = true
# anything ending in .tex is a potential target
TARGETS = $(patsubst %.tex, %.pdf, $(wildcard *.tex) )
# which target to build by default
CURRTARGET = hw4template.pdf

ifeq ($(PDF), true) 
	EXTENSION = pdf
	LATEX = pdflatex
	VIEWER = evince
else 
	EXTENSION = dvi
	LATEX = latex
	VIEWER = xdvi
endif
BIBTEX= bibtex

# where to put extra output files
OUTDIR = ./output
# flags to pass to the latex compiler
FLAGS = -file-line-error -interaction nonstopmode -output-directory=$(OUTDIR)
BIBFLAGS = $(OUTDIR)

##### Build targets #####

# default target; build the document referenced by the CURRTARGET variable
default: $(CURRTARGET)

all: $(TARGETS)

# wildcard rules, so you can just do `make myfilename.pdf` without needing a
# special target for every file
%.pdf: $(OUTDIR)/%.pdf
	cp $< $@

$(OUTDIR)/%.pdf: %.tex | $(OUTDIR)
	$(LATEX) $(FLAGS) $<; \
	$(BIBTEX) $(BIBFLAGS)/$*; \
	$(LATEX) $(FLAGS) $<; \
	$(LATEX) $(FLAGS) $<; \

# Note that we do latex, followed by bibtex, followed by latex two more times.
# The first call builds the document and figures out what references are needed,
# the call to bibtex builds the bibliography items,
# the next call to latex adds the bibliography to the document,
# and the final call fills in the correct references for your \cite{} commands


# convert to postscript 
ps:
ifeq ($(PDF), true)
	pdf2ps $(OUTDIR)/$(CURRTARGET).pdf $(OUTDIR)/$(CURRTARGET).ps 
else
	dvips -o $(OUTDIR)/$(CURRTARGET).ps $(OUTDIR)/$(CURRTARGET).dvi
endif

# convert to PDF
pdf:
ifeq ($(PDF), true)
	#nothing to do
else
	dvipdf $(OUTDIR)/$(CURRTARGET).dvi ; mv $(CURRTARGET).pdf $(OUTDIR) & 
endif

# make sure the output directory exists
$(OUTDIR):
	mkdir $(OUTDIR)

# display current target
view:
	$(VIEWER) $(CURRTARGET) &

# clean up auxiliary output files
clean:
	@echo Cleaning auxiliary files...
	@for i in *.log *.blg *.dvi *.aux *.bbl *.out *~; \
		do rm -f $$i $(OUTDIR)/$$i; \
		done; \
