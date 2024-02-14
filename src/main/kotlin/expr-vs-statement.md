## Expression vs Statement in Kotlin

# Expression
At its core, an expression is a bit of JS code that produces a value. 

* 1 produces ==> 1

* "rohit" produces ==> "rohit"

5*10 produces ==> 50

num > 100 either produces true or false

# An expression can also contain other expression.

## (5 * 10) + 15
* The first expression is the whole which produces 65

* The second expression is (5 * 10) which produces 50

* The third expression are 5, 10 and 15 which evaluates to themselves

***

# Statements

A JavaScript program is a sequence of statements. Each statement is an instruction for the computer to do something.

let hi = "rohit"

throw new Error('Something exploded!');

Here's how I like to think about this: statements are the rigid structure that holds our program together, while expressions fill in the details.

Statements often have "slots" for expressions. We can put any expression we like into those slots.

let hi = /* some expression */;

In terms of valid syntax, expressions are interchangeable. If a statement has an expression slot, we can put any expression there, and the code will run. We won't get a syntax error.


A handy trick to know whether some code is expression or statement we can just console.log it

```javascript
console.log(/* Some code in javascript */)
```

* If it runs, the code is an expression. If you get an error, it's a statement (or, possibly, invalid JS).

As a bonus, we can even see what the expression resolves to, since it'll be printed in the browser console!

This works because all function arguments must be expressions. Expressions produce a value, and that value will be passed into the function. Statements don't produce a value, and so they can't be used as function arguments.

Even as an experienced developer, I rely a ton on console.log. It's a wonderfully versatile tool!

# Statement as a Expression

If a file only contains the following line.
```javascript
1 + 2 + 3
/* expression slot */

// Statement 1:
let hi = /* expression slot */;
// Statement 2:
return /* expression slot */;
// Statement 3:
if (/* expression slot */) { }
// Statement 4:
/* expression slot */
```

Then the code is statement an expression cannot be without any statement but a statement can have only expression.
The statement is essentially empty aside from its expression slot. Our expression 1 + 2 + 3 fills this slot, and our statement is complete



# Enter Statement and Expressions in ReactJs

If you've worked with React before, you're probably aware that squiggly brackets ({ and }) allow us to embed bits of JavaScript within our JSX, like this:
```javascript
function CountdownClock({ secondsRemaining }) {
  return (
    <div>
      Time left:
      {Math.round(secondsRemaining / 60)} minutes!
    </div>
  );
}
```

The Javascript inside the {} is only allowed to be an expression.