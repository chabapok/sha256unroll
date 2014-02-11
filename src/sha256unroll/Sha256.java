package sha256unroll;

import java.util.ArrayList;

/**
 *
 * @author chabapok
 */
public class Sha256 {

    final int BLOCK_SIZE = 64;

    Bits32 h0, h1, h2, h3, h4, h5, h6, h7;

    Bits32[] w = new Bits32[64];

    int count;
    Bits8[] buffer = new Bits8[BLOCK_SIZE];

    private final int[] k_ = {
        0x428a2f98, 0x71374491, 0xb5c0fbcf, 0xe9b5dba5,
        0x3956c25b, 0x59f111f1, 0x923f82a4, 0xab1c5ed5,
        0xd807aa98, 0x12835b01, 0x243185be, 0x550c7dc3,
        0x72be5d74, 0x80deb1fe, 0x9bdc06a7, 0xc19bf174,
        0xe49b69c1, 0xefbe4786, 0x0fc19dc6, 0x240ca1cc,
        0x2de92c6f, 0x4a7484aa, 0x5cb0a9dc, 0x76f988da,
        0x983e5152, 0xa831c66d, 0xb00327c8, 0xbf597fc7,
        0xc6e00bf3, 0xd5a79147, 0x06ca6351, 0x14292967,
        0x27b70a85, 0x2e1b2138, 0x4d2c6dfc, 0x53380d13,
        0x650a7354, 0x766a0abb, 0x81c2c92e, 0x92722c85,
        0xa2bfe8a1, 0xa81a664b, 0xc24b8b70, 0xc76c51a3,
        0xd192e819, 0xd6990624, 0xf40e3585, 0x106aa070,
        0x19a4c116, 0x1e376c08, 0x2748774c, 0x34b0bcb5,
        0x391c0cb3, 0x4ed8aa4a, 0x5b9cca4f, 0x682e6ff3,
        0x748f82ee, 0x78a5636f, 0x84c87814, 0x8cc70208,
        0x90befffa, 0xa4506ceb, 0xbef9a3f7, 0xc67178f2
    };

    private final Bits32 k[];

    Sha256() {
        k = new Bits32[64];
        for (int i = 0; i < 64; i++) {
            k[i] = Bits32.create(k_[i]);
        }
        resetContext();
    }
    
   public Bits8[] digest() {
      Bits8[] tail = padBuffer(); // pad remaining bytes in buffer
      update(tail, 0, tail.length); // last transform of a message
      Bits8[] result = getResult(); // make a result out of context

      reset(); // reset this instance for future re-use

      return result;
   }
   
   
   public void reset() { // reset this instance for future re-use
      count = 0;
      for (int i = 0; i < BLOCK_SIZE; ) {
         buffer[i++] = Bits8.create(0);
      }
      resetContext();
   }


    public void update(Bits8[] b) {
        update(b, 0, b.length);
    }

    public void update(Bits8[] b, int offset, int len) {
        int n = (int) (count % BLOCK_SIZE);
        count += len;
        int partLen = BLOCK_SIZE - n;
        int i = 0;

        if (len >= partLen) {
            System.arraycopy(b, offset, buffer, n, partLen);
            transform(buffer, 0);
            for (i = partLen; i + BLOCK_SIZE - 1 < len; i += BLOCK_SIZE) {
                transform(b, offset + i);
            }
            n = 0;
        }

        if (i < len) {
            System.arraycopy(b, offset + i, buffer, n, len - i);
        }
    }

    protected void transform(Bits8[] in, int offset) {
        Bits32[] result = sha(h0, h1, h2, h3, h4, h5, h6, h7, in, offset);
        h0 = result[0];
        h1 = result[1];
        h2 = result[2];
        h3 = result[3];
        h4 = result[4];
        h5 = result[5];
        h6 = result[6];
        h7 = result[7];
    }

    protected void resetContext() {
        // magic SHA-256 initialisation constants
        h0 = Bits32.create(0x6a09e667);
        h1 = Bits32.create(0xbb67ae85);
        h2 = Bits32.create(0x3c6ef372);
        h3 = Bits32.create(0xa54ff53a);
        h4 = Bits32.create(0x510e527f);
        h5 = Bits32.create(0x9b05688c);
        h6 = Bits32.create(0x1f83d9ab);
        h7 = Bits32.create(0x5be0cd19);
    }

    protected Bits8[] padBuffer() {
        int n = (int) (count % BLOCK_SIZE);
        int padding = (n < 56) ? (56 - n) : (120 - n);
        Bits8[] result = new Bits8[padding + 8];
        
        for(int i=0; i<result.length; i++) result[i] = Bits8.create(0);
        // padding is always binary 1 followed by binary 0s
        result[0] = Bits8.create(0x80);

        // save number of bits, casting the long to an array of 8 bytes
        long bits = count << 3;
        result[padding++] = Bits8.create((byte) (bits >>> 56));
        result[padding++] = Bits8.create((byte) (bits >>> 48));
        result[padding++] = Bits8.create((byte) (bits >>> 40));
        result[padding++] = Bits8.create((byte) (bits >>> 32));
        result[padding++] = Bits8.create((byte) (bits >>> 24));
        result[padding++] = Bits8.create((byte) (bits >>> 16));
        result[padding++] = Bits8.create((byte) (bits >>> 8));
        result[padding] = Bits8.create((byte) (bits));

        return result;
    }

    protected Bits8[] getResult() {

        ArrayList<Bits8> a0 = h0.toBits8Arr();
        ArrayList<Bits8> a1 = h1.toBits8Arr();
        ArrayList<Bits8> a2 = h2.toBits8Arr();
        ArrayList<Bits8> a3 = h3.toBits8Arr();
        ArrayList<Bits8> a4 = h4.toBits8Arr();
        ArrayList<Bits8> a5 = h5.toBits8Arr();
        ArrayList<Bits8> a6 = h6.toBits8Arr();
        ArrayList<Bits8> a7 = h7.toBits8Arr();

        ArrayList<Bits8> result = new ArrayList();

        result.addAll(a0);
        result.addAll(a1);
        result.addAll(a2);
        result.addAll(a3);
        result.addAll(a4);
        result.addAll(a5);
        result.addAll(a6);
        result.addAll(a7);

        return result.toArray(new Bits8[32]);
    }

    private Bits32[]
            sha(Bits32 hh0, Bits32 hh1, Bits32 hh2, Bits32 hh3, Bits32 hh4, Bits32 hh5, Bits32 hh6, Bits32 hh7, Bits8[] in, int offset) {
        Bits32 A = hh0;
        Bits32 B = hh1;
        Bits32 C = hh2;
        Bits32 D = hh3;
        Bits32 E = hh4;
        Bits32 F = hh5;
        Bits32 G = hh6;
        Bits32 H = hh7;

        int r;
        Bits32 T, T2;

        Bits32 S0, S1, Ma, Ch, t1, t2;

        for (r = 0; r < 16; r++) {
            /*
             w[r] = in[offset++]         << 24 |
             (in[offset++] & 0xFF) << 16 |
             (in[offset++] & 0xFF) <<  8 |
             (in[offset++] & 0xFF);
             */

            w[r] = Bits32.fromBits8Arr(in[offset], in[offset + 1], in[offset + 2], in[offset + 3]);
            offset += 4;
        }

        for (r = 16; r < 64; r++) {
            T = w[r - 2];
            T2 = w[r - 15];
            /*
             w[r] = (  ((T >>> 17) | (T << 15))
             ^ ((T >>> 19) | (T << 13))
             ^ (T >>> 10)) +  w[r - 7] + (((T2 >>> 7) | (T2 << 25)) ^ ((T2 >>> 18) | (T2 << 14)) ^ (T2 >>> 3)) + w[r - 16];
             */
            //s0 := (w[i-15] rotr 7) xor (w[i-15] rotr 18) xor (w[i-15] shr 3)
            Bits32 s0 = T2.rr(7).xor(T2.rr(18)).xor(T2.shr(3));
            //s1 := (w[i-2] rotr 17) xor (w[i-2] rotr 19) xor (w[i-2] shr 10)
            Bits32 s1 = T.rr(17).xor(T.rr(19)).xor(T.shr(10));

            w[r] = w[r - 16].add(s0).add(w[r - 7]).add(s1);
        }

        for (r = 0; r < 64; r++) {
          //Σ0 := (a rotr 2) xor (a rotr 13) xor (a rotr 22)
            //Ma := (a and b) xor (a and c) xor (b and c)
            //t2 := Σ0 + Ma
            //Σ1 := (e rotr 6) xor (e rotr 11) xor (e rotr 25)
            //Ch := (e and f) xor ((not e) and g)
            //t1 := h + Σ1 + Ch + k[i] + w[i]
            S0 = A.rr(2).xor(A.rr(13)).xor(A.rr(22));
            Ma = A.and(B).xor(A.and(C)).xor(B.and(C));
            t2 = S0.add(Ma);
            S1 = E.rr(6).xor(E.rr(11)).xor(E.rr(25));
            Ch = E.and(F).xor(G.and(E.not()));
            t1 = H.add(S1).add(Ch).add(k[r]).add(w[r]);
            
            H = G;
            G = F;
            F = E;
            E = D.add(t1);
            D = C;
            C = B;
            B = A;
            A = t1.add(t2);
        }

        Bits32 result[] = new Bits32[]{
            hh0.add(A), hh1.add(B), hh2.add(C), hh3.add(D),
            hh4.add(E), hh5.add(F), hh6.add(G), hh7.add(H)
        };

        //printAll(history, result);
        return result;
    }

}
