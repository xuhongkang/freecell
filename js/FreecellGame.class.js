class FreecellGame {
  constructor(numOpen, numCascade) {
    if (numOpen < 1) {
      throw ("Number of Open Piles Must be Greater or Equal to 1");
    }
    if (numCascade < 4) {
      throw ("Number of Open Piles Must be Greater or Equal to 4");
    }
    this.foundation = [];
    for(let k = 0; k < 4; k++) {
      this.foundation.push([]);
    }
    this.open = [];
    for(let k = 0; k < numOpen; k++) {
      this.open.push([]);
    }
    var deck = getDeck();
    this.cascade = [];
    for(let k = 0; k < numCascade; k++) {
      this.cascade.push([]);
    }
    for(let i = 0; i < deck.length; i++) {
      this.cascade[i % numCascade].push(deck[i]);
    }
  }

  getNumCascade() {
    return this.cascade.length;
  }
  getNumOpen() {
    return this.open.length;
  }
  getFoundation() {
    return this.foundation.map(p => p.slice());
  }
  getOpen() {
    return this.open.map(p => p.slice());
  }
  getCascade() {
    return this.cascade.map(p => p.slice());
  }

  // execute a move from srcPile, e.g. {type:"cascade", index: 0, cardIndex, 5}
  // to destPile, e.g. {type:"open", index: 3}
  // mutates the game state.
  executeMove(srcPile, destPile) {
    let src = null;
    let sSeq = null;
    // Get
    switch (srcPile.type) {
      case "cascade":
        src = this.cascade[srcPile.index];
        sSeq = src.slice(srcPile.cardIndex);
        for (let card in sSeq) {
          src.pop();
        }
        break;
      case "open":
        src = this.open[srcPile.index];
        sSeq = new Array(src[0]);
        src.pop();
        break;
      case "foundation":
        return;
      default:
        return;
    }
    // Add
    let dest = null;
    switch (destPile.type) {
      case "cascade":
        dest = this.cascade[destPile.index];
        for (let i = 0; i < sSeq.length; i ++) {
          let card = sSeq[i];
          dest.push(new Card(card.value, card.suit));
        }
        break;
      case "open":
        dest = this.open[destPile.index];
        dest.push(sSeq[0]);
        break;
      case "foundation":
        console.log(destPile.index);
        dest = this.foundation[destPile.index];
        dest.push(sSeq[0]);
        this._checkWin();
        break;
      default:
        break;
    }
    // .pop(): remove and return last element of array
    // .push(arg): add arg to end of array
  }

  // attempt to stick the given card on either a foundation or an open
  // by finding whatever foundation pile is valid, or the first open pile.
  // return true if success, false if no automatic move available
  // mutates the game state if a move is available.
  attemptAutoMove(srcPile) {
    let src = null;
    let cPile = null;
    switch (srcPile.type) {
      case "cascade":
        cPile = this.cascade[srcPile.index];
        src = cPile[srcPile.cardIndex];
        break;
      case "open":
        cPile = this.open[srcPile.index];
        src = cPile[0];
        break;
      case "foundation":
        cPile = this.foundation[srcPile.index];
        src = cPile[srcPile.cardIndex];
        break;
      default:
        break;
    }
    let foundationIndex = this.getValidFoundation(srcPile);
    if (foundationIndex !== -1) {
      this.foundation[foundationIndex].push(src);
      cPile.pop();
      this._checkWin();
      return true;
    }
    let openIndex = this.getFirstAvailableOpen();
    if (openIndex !== -1) {
      if (srcPile.type != "open") {
        this.open[openIndex].push(src);
        cPile.pop();
        return true;
      } else {
        alert("Card Already in Open Pile, No Available Foundation Piles To Add To");
      }
    }
    return false;
  }

  // return index of first valid foundation destination for the given card,
  // or anything else if there is no valid foundation destination
  getValidFoundation(srcPile) {
    let src = null;
    switch (srcPile.type) {
      case "cascade":
        src = this.cascade[srcPile.index][srcPile.cardIndex];
        break;
      case "open":
        src = this.open[srcPile.index][0];
        break;
      case "foundation":
        return -1;
      default:
        return -1;
    }
    console.log("SrcPile Index : " + srcPile.index);
    console.log("SrcPile Type : " + srcPile.type);
    console.log("SrcPile Value: " + src.value);
    console.log("SrcPile CardIndex: " + srcPile.cardIndex);
    for (let i = 0; i < this.foundation.length; i ++) {
      let fPile = this.foundation[i];
      if (fPile.length != 0) {
        //console.log("Source Value: " + fPile[0].value);
        if (fPile[fPile.length - 1].value == src.value - 1 && fPile[fPile.length - 1].getSuit() == src.getSuit()) {
          return i;
        }
      } else {
        if (src.value == 1) {
          return i;
        }
      }
    }
    return -1;
  }
  // return index of first empty open pile
  // or anything else if no empty open pile
  getFirstAvailableOpen() {
    for (let i = 0; i < this.getNumOpen(); i ++) {
      let fPile = this.getOpen()[i];
      if (fPile.length == 0) {
        return i;
      }
    }
    return -1;
  }

  // return true if in the given cascade pile, starting from given index, there is a valid "build"
  isBuild(pileIdx, cardIdx) {
    let sSeq = this.getCascade()[pileIdx].slice(cardIdx);
    let lastCard = sSeq[0];
    if (sSeq.length > 1) {
      for (let i = 1; i < sSeq.length; i++) {
        let card = sSeq[i];
        if (!FreecellGame._isStackable(card, lastCard)) {
          return false;
        }
        lastCard = card;
      }
    }
    return true;
  }

  // return true if the move from srcPile to destPile would be valid, false otherwise.
  // does NOT mutate the model.
  isValidMove(srcPile, destPile) {
    if (!srcPile || !destPile
        || (srcPile.type == destPile.type && srcPile.index == destPile.index)
        || srcPile.type == "foundation") {
      return false;
    }
    let src = null;
    let sSeq = null;
    // Get
    switch (srcPile.type) {
      case "cascade":
        src = this.getCascade()[srcPile.index];
        sSeq = src.slice(srcPile.cardIndex);
        if (!this.isBuild(srcPile.index, srcPile.cardIndex)) {
          return false;
        }
        break;
      case "open":
        src = this.getOpen()[srcPile.index];
        sSeq = new Array(src[0]);
        break;
      case "foundation":
        return false;
      default:
        return false;
    }


    if (sSeq.length > this._numCanMove(destPile)) {
      alert("In the current state, you are only able to move " + this._numCanMove(destPile) + " card(s) at once.");
      return false;
    }

    // Add
    let dest = null;
    switch (destPile.type) {
      case "cascade":
        dest = this.getCascade()[destPile.index];
        if (dest.length != 0) {
          let lastCard = dest[dest.length - 1];
          let firstCard = sSeq[0];
          if (!FreecellGame._isStackable(firstCard, lastCard)) {
            return false;
          }
        }
        break;
      case "open":
        dest = this.getOpen()[destPile.index];
        if (dest.length != 0) {
          return false;
        }
        if (sSeq.length != 1) {
          return false;
        }
        break;
      case "foundation":
        if (destPile.index == -1) {
          return false;
        }
        dest = this.getFoundation()[destPile.index];
        if (sSeq.length != 1) {
          return false;
        }
        if (dest.length > 13) {
          return false;
        }
        break;
      default:
        return false;
    }
    return true;
  }

  _checkWin() {
    for (let i = 0; i < 4; i++) {
      let deck = this.getFoundation()[i];
      if (deck.length != 13) {
        return;
      }
    }
    alert("Congratulations! You Won!");
  }

  _numCanMove(destPileIndex) {
    // computer number of open piles, number of empty cascade piles
    // apply the formula from Assignment 3
    let n = 0;
    let k = 0;
    for (let i = 0; i < this.getNumOpen(); i ++) {
      let pile = this.getOpen()[i];
      if (pile.length == 0) {
        n += 1;
      }
    }
    for (let i = 0; i < this.getNumCascade(); i ++) {
      let pile = this.getCascade()[i];
      if (pile.length == 0) {
        k += 1;
      }
    }
    if (destPileIndex.type == "cascade") {
      if (this.getCascade()[destPileIndex.index].length == 0) {
        if (n != 0) {
          n -= 1;
        }
      }
    }
    return (n+1) * Math.pow(2, k);
  }

  // is overCard stackable on top of underCard, according to solitaire red-black rules
  static _isStackable(underCard, overCard) {
    if (overCard.value == underCard.value + 1 && overCard.isBlack() != underCard.isBlack()) {
      return true;
    }
    return false;
  }
}

// generate and return a shuffled deck (array) of Cards.
function getDeck() {
  let deck = [];
  let suits = ["spades", "clubs", "diamonds", "hearts"];
  for(let v = 13; v >= 1; v--) {
    for(let s of suits) {
      deck.push(new Card(v, s));
    }
  }
  shuffle(deck);    // comment out this line to not shuffle
  return deck;
}

// shuffle an array: mutate the given array to put its values in random order
function shuffle(array) {
  for (let i = array.length - 1; i > 0; i--) {
    // Pick a remaining element...
    let j = Math.floor(Math.random() * (i + 1));
    // And swap it with the current element.
    [array[i], array[j]] = [array[j], array[i]];
  }
}
