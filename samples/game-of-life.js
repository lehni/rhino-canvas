/*
 * Conway's Game of Life implemented in JavaScript, v1.0
 * http://bendiken.net/scripts/game-of-life/
 *
 * Copyright (c) 2006 Arto Bendiken (arto.bendiken@gmail.com).
 *
 * Permission is hereby granted, free of charge, to any person obtaining a
 * copy of this software and associated documentation files (the "Software"),
 * to deal in the Software without restriction, including without limitation
 * the rights to use, copy, modify, merge, publish, distribute, sublicense,
 * and/or sell copies of the Software, and to permit persons to whom the
 * Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING
 * FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 *
 * 2006-02-26: Initial version using Conway's original 23/3 rules.
 * 2006-10-29: Stefan Haustein: small changes to make the stuff run with rhino-canvas
 *
 */

function ConwaySimulation(cols, rows) {

  this.cells = new Array(rows * cols);

  // Resets the simulation to an initial, randomized state.
  this.reset = function () {
    for (var offset = this.cells.length - 1; offset >= 0; offset--) {
      this.cells[offset] = (Math.random() * 2 > 1.0);
    }
  }

  // Returns a boolean indicating whether the cell at given coordinates
  // is alive or dead. Note that cells outside the grid boundaries are
  // currently considered dead.
  this.cell = function (row, col) {
    return (row >= 0 && row < rows && col >= 0 && col < cols
      && this.cells[row * cols + col]);
  }

  // Advances the simulation by a discrete time step. This is a very
  // straightforward, 'naive' implementation of the Game of Life.
  this.step = function () {
    var next = new Array(rows * cols);
    var changed = false;
    for (var row = rows - 1; row >= 0; row--) {
      var row_offset = row * cols;
      for (var col = cols - 1; col >= 0; col--) {
        var count = 0;
        if (this.cell(row - 1, col - 1)) count++;
        if (this.cell(row - 1, col)) count++;
        if (this.cell(row - 1, col + 1)) count++;
        if (this.cell(row, col - 1)) count++;
        if (this.cell(row, col + 1)) count++;
        if (this.cell(row + 1, col - 1)) count++;
        if (this.cell(row + 1, col)) count++;
        if (this.cell(row + 1, col + 1)) count++;
        var old_state = this.cells[row_offset + col];
        var new_state = (!old_state && count == 3)
          || (old_state && (count == 2 || count == 3));
        if (!changed && new_state != old_state)
          changed = true;
        next[row_offset + col] = new_state;
      }
    }
    this.cells = next;
    return changed; // indicates if simulation is still active
  }

  // Draws the current simulation state onto a 2D Canvas surface.
  this.draw = function (canvas, draw_grid) {
    var ctx = canvas.getContext('2d');
    var width = Math.floor(canvas.width / cols);
    var height = Math.floor(canvas.height / rows);
    // clear the entire canvas
    ctx.fillStyle = 'white';
    ctx.fillRect(0, 0, cols * width, rows * height);
    // draw living cells
    ctx.fillStyle = 'black';
    for (var row = rows - 1; row >= 0; row--) {
      var row_offset = row * cols;
      for (var col = cols - 1; col >= 0; col--) {
        if (this.cells[row_offset + col]) {
          ctx.fillRect(col * width, row * height, width, height);
        }
      }
    }
    // draw grid lines
    if (draw_grid) {
      ctx.fillStyle = 'grey';
      for (var row = 0; row <= rows; row++) {
        ctx.fillRect(0, row * height, cols * width, 1);
        for (var col = 0; col <= cols; col++) {
          ctx.fillRect(col * width, 0, 1, rows * height);
        }
      }
    }
  }

  this.reset();
  return this;
}


function step(){
  cs.step();
  cs.draw(canvas, true);
  setTimeout("step()", 1000);
}


canvas = document.getElementById("canvas");

cs = new ConwaySimulation(20, 20);
step();

