const FS = require('fs');
const _ = require('lodash');

class Claim {
  constructor(id, left, top, width, height) {
    this.id = id;
    this.left = parseInt(left, 10);
    this.top = parseInt(top, 10);
    this.width = parseInt(width, 10);
    this.height = parseInt(height, 10);
    this.area = [];
    this.areaAlreadybooked = false;
  }

  static buildClaim(line) {
    const intermediate = line.split(' @ ');
    const part1 = intermediate[1].split(': ');
    const id = intermediate[0];
    const left = part1[0].split(',')[0];
    const top = part1[0].split(',')[1];
    const width = part1[1].split('x')[0];
    const height = part1[1].split('x')[1];

    return new Claim(id, left, top, width, height);
  }

  markArea() {
    for (let i = this.left; i < this.left + this.width; i += 1) {
      for (let j = this.top; j < this.top + this.height; j += 1) {
        // console.log({i, j});
        this.area.push([i, j]);
      }
    }
    return this;
  }
}

class Fabric {
  constructor() {
    const hash = {};
    for (let i = 0; i <= 999; i += 1) {
      for (let j = 0; j <= 999; j += 1) {
        // console.log({i, j});
        hash[[i, j]] = [];
      }
    }
    this.hash = hash;
  }

  claimArea(claim) {
    claim.area.forEach((area) => {
      this.hash[area].push(claim.id);
    });
  }

  printOverbooked() {
    return Object.keys(this.hash).filter(k => this.hash[k].length >= 2).length;
  }
}

const readContents = () => {
  const rawContents = FS.readFileSync('resources/day3.txt');
  return rawContents.toString();
};

const solve = () => {
  const contents = readContents().split('\n');
  contents.pop(); // because of extra last line

  const fabric = new Fabric();
  const claims = contents
    .map(line => Claim.buildClaim(line))
    .map(claim => claim.markArea());

  claims.forEach(claim => fabric.claimArea(claim));

  // Part 1
  console.log(fabric.printOverbooked());

  // Part 2
  return claims.find((claim) => {
    const carea = claim.area;
    const found = carea.map((ca) => {
      return fabric.hash[ca].length === 1 && fabric.hash[ca][0] === claim.id;
    });
    return _.every(found);
  }).id;
};

module.exports = {
  readContents,
  solve,
};
