/**
 * Copyright © 2016-2019 Lightbend, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *
 * NO COMMERCIAL SUPPORT OR ANY OTHER FORM OF SUPPORT IS OFFERED ON
 * THIS SOFTWARE BY LIGHTBEND, Inc.
 *
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package eroled;


import java.io.IOException;

import static jdk.nashorn.internal.objects.NativeMath.min;

public class SmartOLED extends BasicOLED {
    public SmartOLED() throws IOException, InterruptedException {
        super();
    }


    int[] asciiImages = new int[]{
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // SPACE
            0x00, 0x00, 0x00, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x00, 0x00, 0x18, 0x18, 0x00, 0x00, // !
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x14, 0x28, 0x28, 0x50, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // "
            0x00, 0x00, 0x00, 0x14, 0x14, 0x7E, 0x14, 0x14, 0x28, 0x7E, 0x28, 0x28, 0x28, 0x00, 0x00, 0x00, // //
            0x00, 0x00, 0x10, 0x38, 0x54, 0x54, 0x50, 0x30, 0x18, 0x14, 0x54, 0x54, 0x38, 0x10, 0x10, 0x00, // 0x
            0x00, 0x00, 0x00, 0x44, 0xA4, 0xA8, 0xA8, 0x50, 0x14, 0x2A, 0x2A, 0x4A, 0x44, 0x00, 0x00, 0x00, // %
            0x00, 0x00, 0x00, 0x20, 0x50, 0x50, 0x50, 0x7C, 0xA8, 0xA8, 0x98, 0x88, 0x76, 0x00, 0x00, 0x00, // &
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x18, 0x08, 0x30, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // '
            0x00, 0x00, 0x04, 0x08, 0x10, 0x10, 0x20, 0x20, 0x20, 0x20, 0x20, 0x10, 0x10, 0x08, 0x04, 0x00, // (
            0x00, 0x00, 0x20, 0x10, 0x08, 0x08, 0x04, 0x04, 0x04, 0x04, 0x04, 0x08, 0x08, 0x10, 0x20, 0x00, // )
            0x00, 0x00, 0x00, 0x00, 0x10, 0x10, 0xD6, 0x38, 0x38, 0xD6, 0x10, 0x10, 0x00, 0x00, 0x00, 0x00, // *
            0x00, 0x00, 0x00, 0x00, 0x00, 0x10, 0x10, 0x10, 0xFE, 0x10, 0x10, 0x10, 0x00, 0x00, 0x00, 0x00, // +
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x18, 0x08, 0x30, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // ,
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFE, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // -
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x18, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // .
            0x00, 0x00, 0x02, 0x04, 0x04, 0x04, 0x08, 0x08, 0x10, 0x10, 0x10, 0x20, 0x20, 0x40, 0x40, 0x00, // /

            0x00, 0x00, 0x00, 0x18, 0x24, 0x42, 0x42, 0x42, 0x42, 0x42, 0x42, 0x24, 0x18, 0x00, 0x00, 0x00, // 0
            0x00, 0x00, 0x00, 0x10, 0x70, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x7C, 0x00, 0x00, 0x00, // 1
            0x00, 0x00, 0x00, 0x3C, 0x42, 0x42, 0x02, 0x04, 0x08, 0x10, 0x20, 0x42, 0x7E, 0x00, 0x00, 0x00, // 2
            0x00, 0x00, 0x00, 0x3C, 0x42, 0x42, 0x04, 0x18, 0x04, 0x02, 0x42, 0x42, 0x3C, 0x00, 0x00, 0x00, // 3
            0x00, 0x00, 0x00, 0x08, 0x08, 0x18, 0x28, 0x48, 0x48, 0x7E, 0x08, 0x08, 0x1E, 0x00, 0x00, 0x00, // 4
            0x00, 0x00, 0x00, 0x7E, 0x40, 0x40, 0x5C, 0x62, 0x02, 0x02, 0x42, 0x42, 0x3C, 0x00, 0x00, 0x00, // 5
            0x00, 0x00, 0x00, 0x1C, 0x24, 0x40, 0x40, 0x5C, 0x62, 0x42, 0x42, 0x42, 0x3C, 0x00, 0x00, 0x00, // 6
            0x00, 0x00, 0x00, 0x7E, 0x44, 0x44, 0x08, 0x08, 0x10, 0x10, 0x10, 0x10, 0x10, 0x00, 0x00, 0x00, // 7
            0x00, 0x00, 0x00, 0x3C, 0x42, 0x42, 0x42, 0x3C, 0x24, 0x42, 0x42, 0x42, 0x3C, 0x00, 0x00, 0x00, // 8
            0x00, 0x00, 0x00, 0x38, 0x44, 0x42, 0x42, 0x46, 0x3A, 0x02, 0x02, 0x24, 0x38, 0x00, 0x00, 0x00, // 9

            0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x18, 0x00, 0x00, 0x00, 0x18, 0x18, 0x00, 0x00, 0x00, 0x00, // :
            0x00, 0x00, 0x00, 0x00, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x08, 0x08, 0x10, 0x00, 0x00, 0x00, // ;
            0x00, 0x00, 0x00, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x00, 0x00, // <
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFE, 0x00, 0x00, 0xFE, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // =
            0x00, 0x00, 0x00, 0x40, 0x20, 0x10, 0x08, 0x04, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x00, 0x00, // >
            0x00, 0x00, 0x00, 0x3C, 0x42, 0x42, 0x42, 0x02, 0x04, 0x08, 0x08, 0x00, 0x18, 0x18, 0x00, 0x00, // ?
            0x00, 0x00, 0x00, 0x38, 0x44, 0x9A, 0xAA, 0xAA, 0xAA, 0xAA, 0xB4, 0x42, 0x3C, 0x00, 0x00, 0x00, // @


            0x00, 0x00, 0x00, 0x10, 0x10, 0x28, 0x28, 0x28, 0x28, 0x7C, 0x44, 0x44, 0xEE, 0x00, 0x00, 0x00, // A
            0x00, 0x00, 0x00, 0xFC, 0x42, 0x42, 0x44, 0x78, 0x44, 0x42, 0x42, 0x42, 0xFC, 0x00, 0x00, 0x00, // B
            0x00, 0x00, 0x00, 0x3E, 0x42, 0x82, 0x80, 0x80, 0x80, 0x80, 0x82, 0x44, 0x38, 0x00, 0x00, 0x00, // C
            0x00, 0x00, 0x00, 0xF8, 0x44, 0x42, 0x42, 0x42, 0x42, 0x42, 0x42, 0x44, 0xF8, 0x00, 0x00, 0x00, // D
            0x00, 0x00, 0x00, 0xFC, 0x42, 0x48, 0x48, 0x78, 0x48, 0x48, 0x40, 0x42, 0xFC, 0x00, 0x00, 0x00, // E
            0x00, 0x00, 0x00, 0xFC, 0x42, 0x48, 0x48, 0x78, 0x48, 0x48, 0x40, 0x40, 0xE0, 0x00, 0x00, 0x00, // F
            0x00, 0x00, 0x00, 0x3C, 0x44, 0x84, 0x80, 0x80, 0x80, 0x8E, 0x84, 0x44, 0x38, 0x00, 0x00, 0x00, // G
            0x00, 0x00, 0x00, 0xEE, 0x44, 0x44, 0x44, 0x7C, 0x44, 0x44, 0x44, 0x44, 0xEE, 0x00, 0x00, 0x00, // H
            0x00, 0x00, 0x00, 0x7C, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x7C, 0x00, 0x00, 0x00, // I
            0x00, 0x00, 0x3E, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x08, 0x88, 0xF0, 0x00, 0x00, // J
            0x00, 0x00, 0x00, 0xEE, 0x44, 0x48, 0x50, 0x70, 0x50, 0x48, 0x48, 0x44, 0xEE, 0x00, 0x00, 0x00, // K
            0x00, 0x00, 0x00, 0xE0, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x40, 0x42, 0xFE, 0x00, 0x00, 0x00, // L
            0x00, 0x00, 0x00, 0xEE, 0x6C, 0x6C, 0x6C, 0x54, 0x54, 0x54, 0x54, 0x54, 0xD6, 0x00, 0x00, 0x00, // M
            0x00, 0x00, 0x00, 0xEE, 0x64, 0x64, 0x54, 0x54, 0x54, 0x4C, 0x4C, 0x4C, 0xE4, 0x00, 0x00, 0x00, // N
            0x00, 0x00, 0x00, 0x38, 0x44, 0x82, 0x82, 0x82, 0x82, 0x82, 0x82, 0x44, 0x38, 0x00, 0x00, 0x00, // O
            0x00, 0x00, 0x00, 0xFC, 0x42, 0x42, 0x42, 0x7C, 0x40, 0x40, 0x40, 0x40, 0xE0, 0x00, 0x00, 0x00, // P
            0x00, 0x00, 0x00, 0x38, 0x44, 0x82, 0x82, 0x82, 0x82, 0x82, 0xB2, 0x4C, 0x38, 0x06, 0x00, 0x00, // Q
            0x00, 0x00, 0x00, 0xF8, 0x44, 0x44, 0x44, 0x78, 0x50, 0x48, 0x48, 0x44, 0xE6, 0x00, 0x00, 0x00, // R
            0x00, 0x00, 0x00, 0x3E, 0x42, 0x42, 0x40, 0x30, 0x0C, 0x02, 0x42, 0x42, 0x7C, 0x00, 0x00, 0x00, // S
            0x00, 0x00, 0x00, 0xFE, 0x92, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x38, 0x00, 0x00, 0x00, // T
            0x00, 0x00, 0x00, 0xEE, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x44, 0x38, 0x00, 0x00, 0x00, // U
            0x00, 0x00, 0x00, 0xEE, 0x44, 0x44, 0x44, 0x28, 0x28, 0x28, 0x28, 0x10, 0x10, 0x00, 0x00, 0x00, // V
            0x00, 0x00, 0x00, 0xD6, 0x54, 0x54, 0x54, 0x54, 0x6C, 0x28, 0x28, 0x28, 0x28, 0x00, 0x00, 0x00, // W
            0x00, 0x00, 0x00, 0xEE, 0x44, 0x28, 0x28, 0x10, 0x10, 0x28, 0x28, 0x44, 0xEE, 0x00, 0x00, 0x00, // X
            0x00, 0x00, 0x00, 0xEE, 0x44, 0x44, 0x28, 0x28, 0x10, 0x10, 0x10, 0x10, 0x38, 0x00, 0x00, 0x00, // Y
            0x00, 0x00, 0x00, 0x3E, 0x44, 0x04, 0x08, 0x08, 0x10, 0x10, 0x20, 0x22, 0x7E, 0x00, 0x00, 0x00, // Z

            0x00, 0x00, 0x3C, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x20, 0x3C, 0x00, // [
            0x00, 0x00, 0x40, 0x40, 0x20, 0x20, 0x10, 0x10, 0x10, 0x08, 0x08, 0x04, 0x04, 0x04, 0x02, 0x00, // BACK-SLASH
            0x00, 0x00, 0x3C, 0x04, 0x04, 0x04, 0x04, 0x04, 0x04, 0x04, 0x04, 0x04, 0x04, 0x04, 0x3C, 0x00, // ]
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x24, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // ^
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0xFE, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // _
            0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x30, 0x08, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, 0x00, // `

            0x00, 0x00, 0x00, 0x00, 0x00, 0x38, 0x44, 0x1C, 0x24, 0x44, 0x44, 0x3E, 0x00, 0x00, 0x00, 0x00, // a
            0x00, 0x00, 0x00, 0xC0, 0x40, 0x40, 0x5C, 0x62, 0x42, 0x42, 0x42, 0x42, 0x7C, 0x00, 0x00, 0x00, // b
            0x00, 0x00, 0x00, 0x00, 0x00, 0x1C, 0x22, 0x40, 0x40, 0x40, 0x22, 0x1C, 0x00, 0x00, 0x00, 0x00, // c
            0x00, 0x00, 0x00, 0x0C, 0x04, 0x04, 0x7C, 0x84, 0x84, 0x84, 0x84, 0x8C, 0x76, 0x00, 0x00, 0x00, // d
            0x00, 0x00, 0x00, 0x00, 0x00, 0x3C, 0x42, 0x7E, 0x40, 0x40, 0x42, 0x3C, 0x00, 0x00, 0x00, 0x00, // e
            0x00, 0x00, 0x00, 0x0E, 0x12, 0x10, 0x7C, 0x10, 0x10, 0x10, 0x10, 0x10, 0x7C, 0x00, 0x00, 0x00, // f
            0x00, 0x00, 0x00, 0x00, 0x3E, 0x44, 0x44, 0x38, 0x40, 0x3C, 0x42, 0x42, 0x3C, 0x00, 0x00, 0x00, // g
            0x00, 0x00, 0x00, 0xC0, 0x40, 0x40, 0x5C, 0x62, 0x42, 0x42, 0x42, 0x42, 0xE7, 0x00, 0x00, 0x00, // h
            0x00, 0x00, 0x00, 0x30, 0x30, 0x00, 0x70, 0x10, 0x10, 0x10, 0x10, 0x10, 0x7C, 0x00, 0x00, 0x00, // i
            0x00, 0x00, 0x0C, 0x0C, 0x00, 0x1C, 0x04, 0x04, 0x04, 0x04, 0x04, 0x04, 0x44, 0x78, 0x00, 0x00, // j
            0x00, 0x00, 0x00, 0xC0, 0x40, 0x40, 0x4E, 0x48, 0x50, 0x68, 0x48, 0x44, 0xEE, 0x00, 0x00, 0x00, // k
            0x00, 0x00, 0x00, 0x70, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x7C, 0x00, 0x00, 0x00, // l
            0x00, 0x00, 0x00, 0x00, 0x00, 0xF8, 0x54, 0x54, 0x54, 0x54, 0x54, 0xD6, 0x00, 0x00, 0x00, 0x00, // m
            0x00, 0x00, 0x00, 0x00, 0x00, 0xDC, 0x62, 0x42, 0x42, 0x42, 0x42, 0xE7, 0x00, 0x00, 0x00, 0x00, // n
            0x00, 0x00, 0x00, 0x00, 0x00, 0x18, 0x24, 0x42, 0x42, 0x42, 0x24, 0x18, 0x00, 0x00, 0x00, 0x00, // o
            0x00, 0x00, 0x00, 0x00, 0xDC, 0x62, 0x42, 0x42, 0x42, 0x42, 0x7C, 0x40, 0xE0, 0x00, 0x00, 0x00, // p
            0x00, 0x00, 0x00, 0x00, 0x7C, 0x84, 0x84, 0x84, 0x84, 0x8C, 0x74, 0x04, 0x0E, 0x00, 0x00, 0x00, // q
            0x00, 0x00, 0x00, 0x00, 0x00, 0xEE, 0x32, 0x20, 0x20, 0x20, 0x20, 0xF8, 0x00, 0x00, 0x00, 0x00, // r
            0x00, 0x00, 0x00, 0x00, 0x00, 0x3C, 0x44, 0x40, 0x38, 0x04, 0x44, 0x78, 0x00, 0x00, 0x00, 0x00, // s
            0x00, 0x00, 0x00, 0x00, 0x10, 0x10, 0x7C, 0x10, 0x10, 0x10, 0x10, 0x10, 0x0C, 0x00, 0x00, 0x00, // t
            0x00, 0x00, 0x00, 0x00, 0x00, 0xC6, 0x42, 0x42, 0x42, 0x42, 0x46, 0x3B, 0x00, 0x00, 0x00, 0x00, // u
            0x00, 0x00, 0x00, 0x00, 0x00, 0xE7, 0x42, 0x24, 0x24, 0x28, 0x10, 0x10, 0x00, 0x00, 0x00, 0x00, // v
            0x00, 0x00, 0x00, 0x00, 0x00, 0xD6, 0x54, 0x54, 0x54, 0x28, 0x28, 0x28, 0x00, 0x00, 0x00, 0x00, // w
            0x00, 0x00, 0x00, 0x00, 0x00, 0x6E, 0x24, 0x18, 0x18, 0x18, 0x24, 0x76, 0x00, 0x00, 0x00, 0x00, // x
            0x00, 0x00, 0x00, 0x00, 0xE7, 0x42, 0x24, 0x24, 0x28, 0x18, 0x10, 0x10, 0xE0, 0x00, 0x00, 0x00, // y
            0x00, 0x00, 0x00, 0x00, 0x00, 0x7E, 0x44, 0x08, 0x10, 0x10, 0x22, 0x7E, 0x00, 0x00, 0x00, 0x00, // z
            0x00, 0x00, 0x0c, 0x10, 0x10, 0x10, 0x10, 0x60, 0x10, 0x10, 0x10, 0x10, 0x0c, 0x00, 0x00, 0x00, // {
            0x00, 0x00, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x10, 0x00, // |
            0x00, 0x00, 0x60, 0x10, 0x10, 0x10, 0x10, 0x0c, 0x10, 0x10, 0x10, 0x10, 0x60, 0x00, 0x00, 0x00  // }
    };

    public void drawSingleAscii(int x, int y, char c) throws IOException {
        int ofs = (c - 32) * 16;
        for (int i = 0; i < 16; i++) {
            setRowAddress(y + i);
            setColumnAddress(x);
            writeInstruction(0x5c);
            int str = asciiImages[ofs + i];
            dataProcessing(str);
        }
    }

    private static final int SCREEN_HEIGHT = 4;
    private static final int LINE_HEIGHT = 16;
    private static final int SCREEN_WIDTH = 32;

    public void drawSpreadsheetInColumns(String[][] pStr) throws IOException {
        int lines = (pStr.length / SCREEN_HEIGHT);
        lines += (pStr.length % SCREEN_HEIGHT > 0) ? 1 : 0;
        int columnSize = SCREEN_WIDTH/lines;
        String[] result = new String[SCREEN_HEIGHT];
        for(int i=0; i < pStr.length; i++){
            String key = pStr[i][0];
            String val = pStr[i][1];
            String res = key + generateWhitespaces(columnSize - key.length() - val.length()) + val;
            String oldVal = result[i%SCREEN_HEIGHT];
            if(oldVal!=null) oldVal += COLUMN_DELIMETER+res; else oldVal = res;
            result[i%SCREEN_HEIGHT] = oldVal;
        }
        StringBuffer multiline = new StringBuffer();
        for(String s:result){
            if(s!=null)
            multiline.append(s +"\n");
        }
        drawMultilineString(multiline.toString());
    }

    public void drawKeyValues(String[][] pStr) throws IOException {
        int maxLength = 0;
        String[] result = new String[SCREEN_HEIGHT];
        for(int i=0; i < pStr.length; i++){
            String key = pStr[i][0];
            String val = pStr[i][1];
            //recalculate maxLength for next column
            if(maxLength == 0 || (i%SCREEN_HEIGHT== 0)){
                int tempKeyMax = 0;
                int tempValueMax = 0;
                for(int j=i;j<min(i+SCREEN_HEIGHT,pStr.length);j++){
                    if(tempKeyMax < pStr[j][0].length()) tempKeyMax = pStr[j][0].length();
                    if(tempValueMax < pStr[j][1].length()) tempValueMax = pStr[j][1].length();

                }
                maxLength = tempKeyMax+tempValueMax;
            }

            String res = key + generateWhitespaces(maxLength - key.length() - val.length()) + val;
            String oldVal = result[i%SCREEN_HEIGHT];
            if(oldVal!=null) oldVal += COLUMN_DELIMETER + res; else oldVal = res;
            result[i%SCREEN_HEIGHT] = oldVal;
        }
        StringBuffer multiline = new StringBuffer();
        for(String s:result){
            if(s!=null)
                multiline.append(s +"\n");
        }
        drawMultilineString(multiline.toString());
    }
    private static final String COLUMN_DELIMETER = " | ";

    private String generateWhitespaces(int length){
        StringBuffer outputBuffer = new StringBuffer(length);
        for (int i = 0; i < length; i++){
            outputBuffer.append(" ");
        }
        return outputBuffer.toString();
    }

    public void drawMultilineString(String pStr) throws IOException {
        String[] strings = pStr.split("\n");
        int y = 0;
        for (int i=0;i < 4;i++) {
            String s = (strings.length>i) ? strings[i]: "         ";
            drawString(0, y, s);
            y = y + LINE_HEIGHT;
        }
    }

    public void drawString(String pStr) throws IOException {
        int y = 0;
        for (int x = 0; x < SCREEN_HEIGHT; x++) {
            String temp = pStr.substring(0, Math.min(SCREEN_WIDTH, pStr.length()));
            drawString(x, y, temp);
            pStr = pStr.substring(Math.min(SCREEN_WIDTH, pStr.length()));
            if (pStr.length() == 0) return;
            y = y + LINE_HEIGHT;
        }
    }

    public void drawString(int x, int y, String pStr) throws IOException {
        for(char c: pStr.toCharArray()){
            drawSingleAscii(x,y,c);
            x = x + 2;
        }
    }


    public void dataProcessing(int temp) throws IOException {
        int temp1 = temp & 0x80;
        int temp2 = (temp & 0x40) >> 3;
        int temp3 = (temp & 0x20) << 2;
        int temp4 = (temp & 0x10) >> 1;
        int temp5 = (temp & 0x08) << 4;
        int temp6 = (temp & 0x04) << 1;
        int temp7 = (temp & 0x02) << 6;
        int temp8 = (temp & 0x01) << 3;
        int h11 = temp1 | (temp1 >> 1) | (temp1 >> 2) | (temp1 >> 3);
        int h12 = temp2 | (temp2 >> 1) | (temp2 >> 2) | (temp2 >> 3);
        int h13 = temp3 | (temp3 >> 1) | (temp3 >> 2) | (temp3 >> 3);
        int h14 = temp4 | (temp4 >> 1) | (temp4 >> 2) | (temp4 >> 3);
        int h15 = temp5 | (temp5 >> 1) | (temp5 >> 2) | (temp5 >> 3);
        int h16 = temp6 | (temp6 >> 1) | (temp6 >> 2) | (temp6 >> 3);
        int h17 = temp7 | (temp7 >> 1) | (temp7 >> 2) | (temp7 >> 3);
        int h18 = temp8 | (temp8 >> 1) | (temp8 >> 2) | (temp8 >> 3);
        int d1 = h11 | h12;
        int d2 = h13 | h14;
        int d3 = h15 | h16;
        int d4 = h17 | h18;

        writeData(d1);
        writeData(d2);
        writeData(d3);
        writeData(d4);
    }



}
